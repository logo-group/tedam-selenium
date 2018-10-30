/*
* Copyright 2014-2019 Logo Business Solutions
* (a.k.a. LOGO YAZILIM SAN. VE TIC. A.S)
*
* Licensed under the Apache License, Version 2.0 (the "License"); you may not
* use this file except in compliance with the License. You may obtain a copy of
* the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
* WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
* License for the specific language governing permissions and limitations under
* the License.
*/

package com.lbs.tedam.selenium;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.lbs.tedam.selenium.exception.ElementNotFoundException;
import com.lbs.tedam.selenium.exception.OperationException;

public abstract class Operation {

	private static final String IFRAME = "iframe";

	private OperationParam operationParam;

	public Operation(OperationParam operationParam) {
		this.operationParam = operationParam;
	}

	protected OperationParam getOperationParam() {
		return operationParam;
	}

	protected WebElement findElement(By by) {
		return getOperationParam().getDriver().findElement(by);
	}

	protected By findBy() {
		String tag = getOperationParam().getTag();
		switch (getOperationParam().getType()) {
		case ID:
			return By.id(tag);
		case XPATH:
			return By.xpath(tag);
		case NAME:
			return By.name(tag);
		case INNERTEXT:
			return By.xpath("//*[text()='" + tag + "']");
		}
		return null;
	}

	private void createWait() {
		WebDriver driver = getOperationParam().getDriver();
		driver.switchTo().defaultContent();
		try {
			if (getOperationParam().getType() != null) {
				new WebDriverWait(driver, getOperationParam().getWaitingTime())
						.until(ExpectedConditions.visibilityOfElementLocated(findBy()));
			}
		} catch (TimeoutException e) {
			List<WebElement> iframes = driver.findElements(By.tagName(IFRAME));
			boolean found = createWaitOnFrames(driver, iframes);
			if (!found) {
				throw new ElementNotFoundException(getOperationParam());
			}
		}
	}

	private boolean createWaitOnFrames(WebDriver driver, List<WebElement> iframes) {
		boolean found = false;
		if (iframes.size() > 0) {
			for (WebElement iframe : iframes) {
				try {
					driver.switchTo().frame(iframe);
					new WebDriverWait(driver, getOperationParam().getWaitingTime())
							.until(ExpectedConditions.visibilityOfElementLocated(findBy()));
					found = true;
					break;
				} catch (TimeoutException e1) {
					found |= createWaitOnFrames(driver, driver.findElements(By.tagName(IFRAME)));
					if (found) {
						return true;
					}
					driver.switchTo().parentFrame();
				}
			}
		}
		return found;
	}

	public void executeOperation() throws OperationException {
		createWait();
		doOperation();
	}

	protected abstract OperationResult doOperation() throws OperationException;

}
