package fr.arindam.aws.dynamodb.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.arindam.aws.dynamodb.entity.Company;
import fr.arindam.aws.dynamodb.repository.CompanyRepository;

@RestController
@RequestMapping("/company")
public class Apis {

	@Autowired
	private CompanyRepository companyRepository;
	
	/**
	 * Sending identifier and sort key
	 * @param id
	 * @param companyName
	 * @return
	 */
	@GetMapping("/getcompany/{id}/{companyName}")
	public Company getOneCompany(@PathVariable String id, @PathVariable String companyName){
		return companyRepository.getOneCompany(id,companyName);
	}
	
	@PostMapping("/addnewcompany")
	public String addNewCompany(@RequestBody Company company) {
		return companyRepository.addCompany(company);
	}
	
	@DeleteMapping("/deletecompany")
	public String deleteCompany(@RequestBody Company company) {
		return companyRepository.deleteCompany(company);
	}
	
	@PutMapping("/updatecompany")
	public String updateCompany(@RequestBody Company company) {
		return companyRepository.updateCompany(company);
	}
}
