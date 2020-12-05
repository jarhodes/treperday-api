package app.treperday.api.web;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import app.treperday.api.domain.performance.Attachment;
import app.treperday.api.domain.performance.AttachmentRepository;
import app.treperday.api.domain.performance.Performance;
import app.treperday.api.domain.performance.PerformanceRepository;
import app.treperday.api.domain.user.User;
import app.treperday.api.domain.user.UserRepository;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.DeleteObjectResponse;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@RestController
@RequestMapping("/api/attachment")
public class AttachmentController {

	Logger logger = LoggerFactory.getLogger(AttachmentController.class);

	@Autowired
	private PerformanceRepository performanceRepository;

	@Autowired
	private AttachmentRepository attachmentRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private Path rootLocation;

	@PostMapping("/addtoperformance/{id}")
	public Attachment handleFileUpload(@PathVariable("id") Long performanceId, @RequestParam("file") MultipartFile file,
			@AuthenticationPrincipal UserDetails principal) {

		if (file.getSize() == 0) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "File is empty");
		}

		Path imagePath = this.rootLocation.resolve(file.getOriginalFilename());

		User user = userRepository.findByUsername(principal.getUsername());
		Performance performance = performanceRepository.findById(performanceId)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find resource"));

		if (performance.getUser().getId() == user.getId()) {
			try {
				Files.copy(file.getInputStream(), this.rootLocation.resolve(imagePath));
			} catch (IOException exception) {
				throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to store file");
			}

			String fetchToken = UUID.randomUUID().toString();

			Region region = Region.EU_NORTH_1;
			S3Client s3 = S3Client.builder().region(region).build();

			String bucket = "jonathan3decembertest";

			s3.putObject(PutObjectRequest.builder().bucket(bucket).key(fetchToken).build(), imagePath);

			Attachment attachment = new Attachment(performance, imagePath.toString(), "photo", fetchToken);
			return attachmentRepository.save(attachment);
		} else {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No access to this resource");
		}
	}

	@GetMapping("/list/{id}")
	public List<Attachment> attachmentsByPerformance(@PathVariable Long id,
			@AuthenticationPrincipal UserDetails principal) {

		User user = userRepository.findByUsername(principal.getUsername());
		Performance performance = performanceRepository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find resource"));

		if (performance.getUser().getId() == user.getId()) {
			List<Attachment> attachmentList = attachmentRepository.findByPerformanceId(id);
			if (attachmentList.size() == 0) {
				throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Nothing to return");
			}
			return attachmentList;
		} else {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No access to this resource");
		}
	}

	@GetMapping(value = "/{id}", produces = MediaType.IMAGE_JPEG_VALUE)
	@ResponseBody
	public byte[] serveFileRaw(@PathVariable Long id, @AuthenticationPrincipal UserDetails principal)
			throws IOException {

		User user = userRepository.findByUsername(principal.getUsername());
		Attachment attachment = attachmentRepository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find resource"));

		if (attachment.getPerformance().getUser().getId() == user.getId()) {
			Path file = this.rootLocation.resolve(attachment.getUri());
			try {
				Resource resource = new UrlResource(file.toUri());
				// return ResponseEntity.ok().body(resource);
				return IOUtils.toByteArray(resource.getInputStream());
			} catch (MalformedURLException exception) {
				throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Malformed file url");
			}
		} else {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You cannot access this resource");
		}

	}

	/*
	 * @GetMapping(value = "/getwithtoken/{id}/{token}", produces =
	 * MediaType.IMAGE_JPEG_VALUE) public ResponseEntity<Resource>
	 * serveImage(@PathVariable Long id, @PathVariable String token) {
	 * 
	 * Attachment attachment = attachmentRepository.findByIdAndFetchToken(id, token)
	 * .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
	 * "Unable to find resource"));
	 * 
	 * Path file = this.rootLocation.resolve(attachment.getUri()); try { Resource
	 * resource = new UrlResource(file.toUri()); return
	 * ResponseEntity.ok().body(resource); } catch (MalformedURLException exception)
	 * { throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
	 * "Malformed file url"); } }
	 */

	@GetMapping("/getwithtoken/{id}/{token}")
	public byte[] getWithToken(@PathVariable Long id, @PathVariable String token) {

		Attachment attachment = attachmentRepository.findByIdAndFetchToken(id, token)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find resource"));

		Region region = Region.EU_NORTH_1;
		S3Client s3 = S3Client.builder().region(region).build();
		String bucket = "jonathan3decembertest";

		return s3.getObjectAsBytes(GetObjectRequest.builder().bucket(bucket).key(attachment.getFetchToken()).build())
				.asByteArray();
	}

	// This method would be ideal if the client (in my case a React Native app built
	// with Expo) would send
	// either auth headers or cookies with the request
	// but my client will not do that
	/*
	 * @GetMapping(value = "/get/{id}", produces = MediaType.IMAGE_JPEG_VALUE)
	 * public ResponseEntity<Resource> serveImage(@PathVariable Long
	 * id, @AuthenticationPrincipal UserDetails principal) {
	 * 
	 * User user = userRepository.findByUsername(principal.getUsername());
	 * Attachment attachment = attachmentRepository.findById(id) .orElseThrow(() ->
	 * new ResponseStatusException(HttpStatus.NOT_FOUND,
	 * "Unable to find resource"));
	 * 
	 * if (attachment.getPerformance().getUser().getId() == user.getId()) { Path
	 * file = this.rootLocation.resolve(attachment.getUri()); try { Resource
	 * resource = new UrlResource(file.toUri()); return
	 * ResponseEntity.ok().body(resource); } catch (MalformedURLException exception)
	 * { throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
	 * "Malformed file url"); } } else { throw new
	 * ResponseStatusException(HttpStatus.FORBIDDEN,
	 * "You cannot access this resource"); } }
	 */

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<Long> deleteAttachment(@PathVariable Long id,
			@AuthenticationPrincipal UserDetails principal) {

		User user = userRepository.findByUsername(principal.getUsername());
		Attachment attachment = attachmentRepository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find resource"));

		if (attachment.getPerformance().getUser().getId() == user.getId()) {

			Region region = Region.EU_NORTH_1;
			S3Client s3 = S3Client.builder().region(region).build();
			String bucket = "jonathan3decembertest";
			DeleteObjectResponse response = s3
					.deleteObject(DeleteObjectRequest.builder().bucket(bucket).key(attachment.getFetchToken()).build());

			Path file = this.rootLocation.resolve(attachment.getUri());
			try {
				Files.delete(file);
			} catch (IOException exception) {
				throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Problem deleting file on server");
			}
			attachmentRepository.delete(attachment);
			return new ResponseEntity<>(id, HttpStatus.OK);
		} else {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You cannot access this resource");
		}
	}

	@GetMapping("/details/{id}")
	public Attachment getDetails(@PathVariable Long id, @AuthenticationPrincipal UserDetails principal) {
		
		User user = userRepository.findByUsername(principal.getUsername());
		Attachment attachment = attachmentRepository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find resource"));;
		
		if (attachment.getPerformance().getUser().getId() == user.getId()) {
			return attachment;
		}
		else {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You cannot access this resource");
		}
	}

}
