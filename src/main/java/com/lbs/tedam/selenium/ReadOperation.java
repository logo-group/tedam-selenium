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

import com.lbs.tedam.selenium.exception.ElementNotFoundException;
import com.lbs.tedam.selenium.exception.OperationException;
import com.lbs.tedam.selenium.exception.ReadOperationException;
import com.lbs.tedam.selenium.exception.ReadOperationVerifyException;

public class ReadOperation extends Operation {

	public ReadOperation(OperationParam operationParam) {
		super(operationParam);
	}

	@Override
	public OperationResult doOperation() throws OperationException {
		try {
			String actualValue = findElement(findBy()).getAttribute("value");
			String expectedValue = getOperationParam().getValue();
			if (actualValue.compareTo(expectedValue) != 0) {
				throw new ReadOperationVerifyException(actualValue, getOperationParam());
			}
			return new OperationResult(actualValue);
		} catch (NoSuchElementException e) {
			throw new ElementNotFoundException(getOperationParam());
		} catch (ReadOperationVerifyException e) {
			throw e;
		} catch (Exception e) {
			throw new ReadOperationException(getOperationParam());
		}
	}

}
