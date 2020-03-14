package fr.arindam.aws.dynamodb.repository;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBSaveExpression;
import com.amazonaws.services.dynamodbv2.model.ConditionalCheckFailedException;
import com.amazonaws.services.dynamodbv2.model.ConditionalOperator;
import com.amazonaws.services.dynamodbv2.model.ExpectedAttributeValue;

import fr.arindam.aws.dynamodb.entity.Company;

@Repository
public class CompanyRepository {

	@Autowired
	private DynamoDBMapper mapper;
	
	public Company getOneCompany(String id, String companyName){
		return mapper.load(Company.class, id, companyName);
	}
	
	public String addCompany(Company company) {		
		mapper.save(company);		
		return "Successfully added";
	}
	
	public String deleteCompany(Company company) {		
		mapper.delete(company);		
		return "Successfully deleted";
	}
	
	public String updateCompany(Company company) {
		try {
			mapper.save(company, buildDynamoDBExpression(company));
			
		}
		catch(ConditionalCheckFailedException exception) {
			exception.printStackTrace();
			return "Item could not found, save failed "+exception.getMessage();
		}
		return "Successfully updated";
	}
	
	private DynamoDBSaveExpression buildDynamoDBExpression(Company company) {
		DynamoDBSaveExpression saveExpression = new DynamoDBSaveExpression();
		Map<String, ExpectedAttributeValue> expectedAttributes = new HashMap<String, ExpectedAttributeValue>();
		expectedAttributes.put("hashKey", new ExpectedAttributeValue(false));
		expectedAttributes.put("rangeKey", new ExpectedAttributeValue(false));		        
		saveExpression.setExpected(expectedAttributes);
		saveExpression.setConditionalOperator(ConditionalOperator.AND);
		return saveExpression;
	}
}
