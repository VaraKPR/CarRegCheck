package TestNGTest;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.Test;

public class CarRegistrationCheck {
	  FileReader fr = null; BufferedReader br = null;
	  
	  String readLine; String filePath; StringBuffer sb = new StringBuffer();
	  String line;
	  
	  List<String> carregNumList= new ArrayList<>();
	  public StringBuffer readinputTextFile(String filepath) throws Exception {
	  
	  fr = new FileReader(filepath); br = new BufferedReader(fr);
	  
	  while((readLine = br.readLine())!=null) {
	  
	  line = readLine;
	
		 Pattern p1 = Pattern.compile("[A-Z][A-Z][0-9][0-9]+[A-Z]{3,3}");
		 Pattern p2 = Pattern.compile("[A-Z][A-Z][0-9][0-9] +[A-Z]{3,3}");
		 
		  Matcher m1 = p1.matcher(line);
		  Matcher m2 = p2.matcher(line);
		  //System.out.println(line);
		  while(m1.find() )  
			  carregNumList.add(m1.group());	
		  while(m2.find() )  
			  carregNumList.add(m2.group());
	  }
	    return sb;  
	  }
	  //This is the code to read output file
	  public StringBuffer readoutputTextFile(String filepath) throws Exception {
		  fr = new FileReader(filepath); br = new BufferedReader(fr); 
		  while((readLine = br.readLine())!=null) { 
			  line = readLine; 
			  sb.append(readLine+"\n");
		  }
		    return sb;   
	  }
	  
	  @Test(priority=1)
	  //Trying to verify registration number
	  public void readAndExtractInputFile() throws Exception {
	  StringBuffer carinput =
	  readinputTextFile("C:\\Users\\varap\\Downloads\\FW__New_test (1)\\car_input.txt");
	  }	
	
	
	public WebDriver driver;
	
	@Test(priority=2) 
	public void verifyRegistrationNumbers() throws InterruptedException {
		
		 StringBuffer caroutput = null;
		try {
			caroutput = readoutputTextFile("C:\\Users\\varap\\Downloads\\FW__New_test (1)\\car_output.txt");
		} catch (Exception e) {
			e.printStackTrace();
		}	 
			//opening the webdriver and passing the registration number to website  
		for  (int i=0;i<carregNumList.size();i++) {
			System.setProperty("webdriver.chrome.driver",
					"C:\\Users\\varap\\Downloads\\chromedriver_win32 (1)\\chromedriver.exe");
			WebDriver driver = new ChromeDriver();
			driver.get("C:\\Users\\varap\\Downloads\\FW__New_test (1)\\car_output.txt");

			driver.get("https://cartaxcheck.co.uk/");
			driver.manage().window().maximize();
			Thread.sleep(2000);				
			//String [] regnum1 = sb.toString().split("\\n");
			driver.findElement(By.id("vrm-input")).sendKeys(carregNumList.get(i));
			Thread.sleep(2000);
			driver.findElement(By.xpath(".//button[contains(text(),'Free Car Check')]")).click();
			Thread.sleep(2000);
			
			List<WebElement> obj= driver.findElements(By.xpath("//*[@id=\"m\"]/div/div[3]/div[1]/div"));
			//getting the actual data from the website
			for (WebElement obelement : obj) {
				String actualdata=null;
				String elements = obelement.getText();
				
				List<String> elementList =new ArrayList<>();
				String[] elementsbyline=elements.toString().split("\\n");
		
				if (elementsbyline.length>9) {
					 actualdata= elementsbyline[2]+","+elementsbyline[4]+","+elementsbyline[6]+","+elementsbyline[8]+","+elementsbyline[10];	
				}
				String[] caroutputbyregExpected=caroutput.toString().split("\\n");
				String carregnumber = carregNumList.get(i);
				String carregnumberTrim = carregnumber.replaceAll("\\s", "");
				
				//verifying for each car registration number extracted from input file and validating against the website to the ouput file
				boolean testsuccessfull = false;
				for (int j=0;j<=4;j++) {
					
					if (caroutputbyregExpected[j].contains(carregnumberTrim)) {
						//test becomes successful, if actual data matches with output data
						if (caroutputbyregExpected[j].equalsIgnoreCase(actualdata))
							testsuccessfull = true;
					break;
					
					}
				}
				
				if (testsuccessfull)
					System.out.println("TEST SUCCESSFUL FOR :"+carregnumberTrim);
				else 
					System.out.println("TEST FAILED FOR :"+carregnumberTrim);
				driver.quit();		
			}	
		}	
		}
}
			
	


