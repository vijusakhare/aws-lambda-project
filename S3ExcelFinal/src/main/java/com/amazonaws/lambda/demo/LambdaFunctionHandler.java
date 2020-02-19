package com.amazonaws.lambda.demo;

import java.io.File;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;

public class LambdaFunctionHandler implements RequestHandler<S3Event, String> {

    private AmazonS3 s3 = AmazonS3ClientBuilder.standard().build();

    public LambdaFunctionHandler() {}

  
    LambdaFunctionHandler(AmazonS3 s3) {
        this.s3 = s3;
    }

    @Override
    public String handleRequest(S3Event event, Context context) {
        context.getLogger().log("Received event: " + event);
        
        try
        {
        	BasicAWSCredentials awscreds = new BasicAWSCredentials("AKIAXBLMNOPS5FMDAQVB","jhmu77Rv8z/CXKmyp3CcN8munRJ/7W1Wm08GqtnY");
			 
			 //AmazonS3 s3client = AmazonS3ClientBuilder.standard().withRegion("ap-south-1").awsCredentials(new AWSStaticCredentialProvider(creds));
			 AmazonS3 s3Client = AmazonS3ClientBuilder.standard()   
	    	    	    .withRegion("ap-south-1")
	    	    	    .withCredentials(new AWSStaticCredentialsProvider(awscreds))
	    	    	    .build();
			 
			 String bucketname = "dviju";
			 String foldername = "folder1";
			 
			 String filename = "nice.xlsx";
			 String filenameLocalIPC = "C:\\Users\\svijaykumar\\Documents\\nice.xlsx";
			 
			 PutObjectRequest request = new PutObjectRequest(bucketname,foldername+"/"+filename,new File(filenameLocalIPC));
			 s3Client.putObject(request);
			 System.out.println("Uploading File Done...!!!!");
			 
			 //Get File From S3 bucket	 
			 S3Object fullObject;
			 fullObject = s3Client.getObject(new GetObjectRequest(bucketname,foldername+"/"+filename));	 
			 System.out.println("Downloading file done...!!");
			
			 S3ObjectInputStream s3ObjectInputStream = fullObject.getObjectContent();
			 
			 XSSFWorkbook workbook = new XSSFWorkbook(s3ObjectInputStream);
			 XSSFSheet sheet = workbook.getSheetAt(0);
			 
			 
			 
			    Iterator<Row> rowIterator = sheet.iterator();
	            while (rowIterator.hasNext()) 
	            {
	                Row row = rowIterator.next();
	                //For each row, iterate through all the columns
	                Iterator<Cell> cellIterator = row.cellIterator();
	                 
	                while (cellIterator.hasNext()) 
	                {
	                    Cell cell = cellIterator.next();
	                    //Check the cell type and format accordingly
	                    switch (cell.getCellType()) 
	                    {
	                        case Cell.CELL_TYPE_NUMERIC:
	                            System.out.print(cell.getNumericCellValue()+"   ");
	                            break;
	                        case Cell.CELL_TYPE_STRING:
	                            System.out.print(cell.getStringCellValue()+"   ");
	                            break;
	                    }
	                }
	                System.out.println("");
	            }
			 
        }catch(Exception e)
        {
        	System.out.println(e);
        }
        
      return "ok";
    }
}