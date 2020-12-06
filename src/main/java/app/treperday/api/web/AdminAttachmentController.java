package app.treperday.api.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import app.treperday.api.domain.performance.Attachment;
import app.treperday.api.domain.performance.AttachmentRepository;
import app.treperday.api.domain.user.UserRepository;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;

@Controller
@RequestMapping("/admin/attachment")
public class AdminAttachmentController {

	@Autowired
	private AttachmentRepository attachmentRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@GetMapping("")
	public String attachmentList(Model model) {
		model.addAttribute("attachments", attachmentRepository.findAll());
		return "admin/attachmentlist";
	}
	
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
	

}
