package dataset;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class Dataset {

    public static void main(String[] args) throws InterruptedException, IOException {
        System.setProperty("webdriver.edge.driver","msedgedriver.exe");
//        System.setProperty("webdriver.chrome.driver","chromedriver.exe");

//        ChromeOptions chromeOptions = new ChromeOptions();
//        chromeOptions.setHeadless(true);
        PrintWriter printWriter = new PrintWriter(new File("priorBlocks.csv"));

        WebDriver driver = new EdgeDriver();
        List<String> priorBlocks = new ArrayList<>();
        for (int i = 10985550; i < 10985851; i++) {
            driver.get("https://etherscan.io/block/"+i);
            WebElement webElement = driver.findElement(By.cssSelector("#ContentPlaceHolder1_maintable > div:nth-child(3) > div.col-md-9"));
            String timestamp = (webElement.getText().substring(webElement.getText().indexOf('(')+1,webElement.getText().indexOf(')')));
            webElement = driver.findElement(By.cssSelector("#ContentPlaceHolder1_maintable > div:nth-child(6) > div.col-md-9"));
            String time2mine = (webElement.getText().substring(webElement.getText().indexOf(" in ")+4,webElement.getText().lastIndexOf(" ")));
            printWriter.println(i+","+timestamp+","+time2mine);
            System.out.println((i+","+timestamp+","+time2mine));
        }
        printWriter.close();
//        driver.get("https://etherscan.io/txsPending");
//        driver.navigate().to("https://etherscan.io/txsPending?ps=100&&m=&p=1");
//        System.out.println(driver.getTitle());
//        List<String> transactionHashes = new ArrayList<>();
//

//        for (int i = 1; i <= 100; i++) {
//            WebElement webElement = driver.findElement(By.cssSelector("#transfers > div.table-responsive.mb-2.mb-md-0 > table > tbody > tr:nth-child("+i+") > td:nth-child(1) > span > a"));
//            transactionHashes.add(webElement.getText());
//        }
//
//        List<Transaction> transactions = new ArrayList<>();

//        for (int i = 1; i <= 100; i++) {
//            driver.navigate().to("https://etherscan.io/tx/"+transactionHashes.get(i-1));
//            WebElement webElement = driver.findElement(By.cssSelector("#ContentPlaceHolder1_maintable > div:nth-child(7) > div.col-md-9"));
//            System.out.println(webElement.getText());
//
//            Transaction transaction = new Transaction(transactionHashes.get(i-1),webElement.getText());
//            transactions.add(transaction);
//        }
//        System.out.println(transactionHashes);
//        Database database = new Database();
//        database.setTransactions(transactions);
//        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
//        objectMapper.writeValue(new File("transaction.json"),database);

    }
}
