package application;

import java.util.ArrayList;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.cloudwatchevents.AmazonCloudWatchEventsClient;
import com.amazonaws.services.cloudwatchevents.model.PutRuleRequest;
import com.amazonaws.services.cloudwatchevents.model.PutRuleResult;
import com.amazonaws.services.cloudwatchevents.model.PutTargetsRequest;
import com.amazonaws.services.cloudwatchevents.model.PutTargetsResult;
import com.amazonaws.services.cloudwatchevents.model.Target;
import com.amazonaws.services.lambda.AWSLambdaClient;
import com.amazonaws.services.lambda.model.AddPermissionRequest;

public class CloudWatchConnector {
	//Wen Hao's Keys
	private static final String accessKey = "AKIAJICGFKMYAER4HNEQ";
	private static final String secretAccessKey = "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";
	
	private AWSCredentials credentials;
	private AmazonCloudWatchEventsClient client;
	private AWSLambdaClient lambdaClient;
	
	private static final String lambdaName = "Hello";
	private static final String lambdaARN = "arn:aws:lambda:ap-southeast-1:181848607663:function:Hello";
	
	
	public CloudWatchConnector(){
		init();
	}
	
	private void init(){
		credentials = new BasicAWSCredentials(accessKey, secretAccessKey);
		client = new AmazonCloudWatchEventsClient(credentials);
		client.withRegion(Regions.AP_SOUTHEAST_1);
		lambdaClient = new AWSLambdaClient(credentials);
		lambdaClient.withRegion(Regions.AP_SOUTHEAST_1);
		System.out.println("Logged in");
	}
	
	
	//Create new eventRule, return arn //e.g ruleName: "JavaRule", expression: "rate(1 minutes)"
	//If a rule with the same ruleName exist, will overwrite old rule
	private String createScheduleRule(String ruleName, String scheduleExpression){
		PutRuleRequest newRequest = new PutRuleRequest().withName(ruleName);
		newRequest.setScheduleExpression(scheduleExpression);
		newRequest.setState("ENABLED");
		
		PutRuleResult result = this.client.putRule(newRequest);
		return result.getRuleArn();
	}
	
	//Add new Target to Rule
	//Target Name = name of lambda function ARN
	private void addTargetToRule(String functionName, String functionARN, String ruleName, String input){
		//form request
		PutTargetsRequest request = new PutTargetsRequest().withRule(ruleName);
		Target target = new Target();
		target.setArn(functionARN);
		target.setId(ruleName+functionName);
		target.setInput(input);
		ArrayList<Target> listTarget = new ArrayList<Target>();
		listTarget.add(target);
		request.setTargets(listTarget);
		
		//form result
		client.putTargets(request);
	}
	
	//Repo = rule name of event rule
	//Interval = schedule expression
	//Input = json input
	public void addScheduledEvent(String repo, String scheduleExpression, String input){
		createScheduleRule(repo, scheduleExpression);
		addTargetToRule(lambdaName, lambdaARN, repo, input);
	}
}
