package selenium1;

import java.util.List;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class CanadianTireTest1 {

	public static List<WebElement> mainMenuList;
	public static int index;
	public static boolean isDisplayed;
	public static List<WebElement> sectionList;
	public static List<WebElement> subSectionList;
	public static List<WebElement> productCategoryList;
	public static List<WebElement> brandNameList;
	public static List<WebElement> ratingsRangeList;
	public static List<WebElement> productList;
	public static List<WebElement> productWithSavingsList;
	public static List<WebElement> addToCartList;
	
	public static void main(String[] args) {
		System.setProperty("webdriver.chrome.driver", "C:\\Selenium_jars\\ChromeDriver\\chromedriver.exe");
		WebDriver driver = new ChromeDriver();
		WebDriverWait wait = new WebDriverWait(driver, 20);
		setBaseUrl(driver);
		mainMenu(driver, "HOME & ESSENTIALS"); //printing main menu items and selecting "HOME & ESSENTIALS"
		section(driver, "KITCHEN", "Microwave"); //Subsection - Kitchen and product is Microwave
		searchProductByRatingRange(driver, wait, "4"); //ratingRange is 4
		searchProductByBrand(driver, "Master Chef");
		addProductToWishList(driver, wait);
	}	
	
	public static void setBaseUrl(WebDriver driver) {
		driver.get("https://www.canadiantire.ca/en.html");
		driver.manage().window().maximize();
		driver.manage().deleteAllCookies();
		driver.navigate().refresh();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	}
	
	public static void mainMenu(WebDriver driver, String mainMenuItem) {
		mainMenuList = driver.findElements(By.xpath("//ul[@class = 'departments-megamenu__nav-list']//li//span"));
		printListItemNames(mainMenuList, "getText", "", true);
		index = printListItemNames(mainMenuList, "getText", mainMenuItem, false);
		mainMenuList.get(index).click();
		driver.manage().timeouts().implicitlyWait(6, TimeUnit.SECONDS);
		driver.navigate().refresh();
	}
	
	public static void section(WebDriver driver, String sectionName, String itemTypeName) {
		sectionList = driver.findElements(By.xpath("//div[@class = 'list-of-links__main-link-wrapper']"));
		printListItemNames(sectionList, "getText", "", true);
		for (int i = 0; i < sectionList.size(); i++) {
			subSectionList = sectionList.get(i).findElements(By.xpath("following-sibling::ul//li"));
			printListItemNames(subSectionList, "getText", "", true);
		}
		index = printListItemNames(sectionList, "getText", sectionName, false);
		sectionList.get(index).findElement(By.xpath("following-sibling::ul//following-sibling::div//a[@title = 'View All']")).click();
		driver.manage().timeouts().implicitlyWait(6, TimeUnit.SECONDS);
		productCategoryList = driver.findElements(By.xpath("//div[@class = 'banner-builder__container']//a"));
		System.out.println(productCategoryList.size());
		index = printListItemNames(productCategoryList, "title", itemTypeName, false);
		if (index > -1) {
			productCategoryList.get(index).click();
		}
		else {
			System.out.println("Product not found");
			closeWindow(driver);
		}
	}
	
	public static void searchProductByBrand(WebDriver driver, String brandName) {
		driver.manage().timeouts().implicitlyWait(6, TimeUnit.SECONDS);
		brandNameList = driver.findElements(By.xpath("//li[@id = 'brandname']//ul"));
		index = printListItemNames(brandNameList, "title", brandName, true);
		if (index > -1) {
			brandNameList.get(index).click();
		}
		else {
			System.out.println("Product not found in searchProductByBrand");
		}
		
	}
	
	public static void searchProductByRatingRange(WebDriver driver, WebDriverWait wait, String ratingRange) {
		driver.manage().timeouts().implicitlyWait(6, TimeUnit.SECONDS);
		ratingsRangeList = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("//li[@id = 'ratingsrange']//li")));		 
		index = printListItemNames(ratingsRangeList, "data-analytics", ratingRange, false);
		if (index > -1) {
			ratingsRangeList.get(index).click();
			productList = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("//div[@class= 'temporary-grid-item']")));
			System.out.println("Total product with " + ratingRange + " stars: " + productList.size());
		}
		else {
			System.out.println("Product not found in searchProductByRatingRange");
			//closeWindow(driver);
		}
		driver.navigate().refresh();
	}
	
	public static void addProductToWishList(WebDriver driver, WebDriverWait wait) {
		productWithSavingsList = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath
				("//div[@class= 'temporary-grid-item']//div[@class ='price__tag']")));
		System.out.println("Total products with savings: " + productWithSavingsList.size() );
		printListItemNames(productWithSavingsList, "getText", "", true);
		driver.navigate().refresh();
		addToCartList = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("//div[@class= 'temporary-grid-item']//span[contains(text(), 'Add To Cart')]")));
		System.out.println("Total products to add to Cart: " + addToCartList.size() );
		addToCartList.get(1).click();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);	
		isDisplayed = driver.findElement(By.xpath("//span[contains(text(), 'Save to Wishlist')]")).isDisplayed();
		if(isDisplayed) {
			driver.findElement(By.xpath("//span[contains(text(), 'Save to Wishlist')]")).click();
		}
		else {
			System.out.println("Save to WishList is not displayed");
		}
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.findElement(By.xpath("//button[@title = 'Close popup window']")).click();
	}
	
	
	private static void closeWindow(WebDriver driver) {
		driver.close();
		System.out.println("End of session");
	}

	public static int printListItemNames(List<WebElement> list, String attribute, String searchStr, boolean print) {
		String itemText = "";
		String printText = "";
		int index = -1;
		if (list.size() == 0) {
			printText = "No items in this list";
			return index;
		}
		for (int i = 0; i < list.size(); i++) {
			if (attribute == "getText") itemText = list.get(i).getText();
			else itemText = list.get(i).getAttribute(attribute);
			if (!itemText.isEmpty()) printText = printText + "\n" + itemText; 
			if(itemText.contains(searchStr) && !searchStr.isEmpty() && !itemText.isEmpty() && print == false) {
				index = i;
				return index;
			}
		}

		if(print) {
			System.out.println(printText);
		}
		return index;
	}
}
