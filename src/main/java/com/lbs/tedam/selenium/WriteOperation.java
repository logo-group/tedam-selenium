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

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;

import com.lbs.tedam.selenium.exception.ElementNotFoundException;
import com.lbs.tedam.selenium.exception.OperationException;
import com.lbs.tedam.selenium.exception.WriteOperationException;

public class WriteOperation extends Operation {

	public WriteOperation(OperationParam operationParam) {
		super(operationParam);
	}

	@Override
	public OperationResult doOperation() throws OperationException {
		try {
			WebElement element = findElement(findBy());
			element.sendKeys(Keys.NULL);
			element.sendKeys(Keys.chord(Keys.CONTROL, "a"));
			element.sendKeys(getOperationParam().getValue());
			return new OperationResult(null);
		} catch (NoSuchElementException e) {
			throw new ElementNotFoundException(getOperationParam());
		} catch (Exception e) {
			throw new WriteOperationException(getOperationParam());
		}
	}

}
