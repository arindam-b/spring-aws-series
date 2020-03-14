package fr.arindam.aws.rds.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import fr.arindam.aws.rds.entity.Projects;
import fr.arindam.aws.rds.repository.ProjectRepository;

@RestController
public class ProjectController {

	@Autowired
	private ProjectRepository projectRepository;
	
	@GetMapping("/getallprojects")
	public List<Projects> getAllProjects() {
		return projectRepository.findAll();
	}
	
	@PostMapping("/addnewproject")
	public String saveNewProject(@RequestBody Projects project) {	
		projectRepository.save(project);		
		return "Added successfully";
	}
	
}
