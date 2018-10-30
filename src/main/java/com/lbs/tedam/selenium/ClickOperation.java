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

import java.util.NoSuchElementException;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.UnhandledAlertException;
import org.openqa.selenium.WebElement;

import com.lbs.tedam.selenium.exception.ClickOperationException;
import com.lbs.tedam.selenium.exception.ElementNotFoundException;
import com.lbs.tedam.selenium.exception.OperationException;

public class ClickOperation extends Operation {

	private static final String SPAN = "span";

	public ClickOperation(OperationParam operationParam) {
		super(operationParam);
	}

	@Override
	public OperationResult doOperation() throws OperationException {
		WebElement element = null;
		try {
			element = findElement(findBy());
			doOperationByTagName(element);
			return new OperationResult(null);
		} catch (NoSuchElementException e) {
			throw new ElementNotFoundException(getOperationParam());
		} catch (UnhandledAlertException e) {
			throw e;
		} catch (Exception e) {
			try {
				JavascriptExecutor executor = (JavascriptExecutor) getOperationParam().getDriver();
				executor.executeScript("arguments[0].click();", element);
				return new OperationResult(null);
			} catch (Exception e1) {
				throw new ClickOperationException(getOperationParam());
			}
		}
	}

	private void doOperationByTagName(WebElement element) {
		switch (element.getTagName()) {
		case SPAN:
			element.click();
			break;
		default:
			element.sendKeys(Keys.NULL);
			element.click();
			break;
		}
		try {
			getOperationParam().getDriver().switchTo().alert();
			throw new UnhandledAlertException("Alert exist!"); // Throw to SeleniumManager.handleExecution
		} catch (NoAlertPresentException e) {
		}
	}

}
