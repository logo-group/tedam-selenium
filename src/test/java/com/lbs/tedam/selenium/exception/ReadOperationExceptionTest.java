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

package com.lbs.tedam.selenium.exception;

import org.junit.Test;

import com.lbs.tedam.selenium.OperationParam;
import com.lbs.tedam.util.EnumsV2.SeleniumByType;

public class ReadOperationExceptionTest {

	@Test
	public void testClass() {
		OperationParam operationParam = new OperationParam(null, 0, SeleniumByType.XPATH, "BUTTON", null, null, null);
		ReadOperationException exception = new ReadOperationException(operationParam);
		System.out.println(exception.getMessage());
	}

}
