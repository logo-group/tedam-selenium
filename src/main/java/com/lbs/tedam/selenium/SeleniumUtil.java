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

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lbs.tedam.bsh.utils.ScriptService;
import com.lbs.tedam.model.SnapshotValue;
import com.lbs.tedam.util.Constants;
import com.lbs.tedam.util.Enums.Regex;
import com.lbs.tedam.util.Enums.ScriptParameterSahi;
import com.lbs.tedam.util.EnumsV2.SeleniumByType;
import com.lbs.tedam.util.EnumsV2.TestStepType;
import com.lbs.tedam.util.HasLogger;

public class SeleniumUtil implements HasLogger {

	private ScriptService scriptService;
	private int testStepId = 0;

	public SeleniumUtil(ScriptService scriptService) {
		this.scriptService = scriptService;
	}

	@SuppressWarnings("unchecked")
	public List<Operation> createOperationList(Map<String, Object> parameterMap) {
		List<Operation> operationList = new ArrayList<>();
		List<SeleniumParametersDTO> parametersDTOList = (List<SeleniumParametersDTO>) parameterMap
				.get(Constants.WEB_SCRIPT_PARAMETERS_DTO_LIST);
		Map<ScriptParameterSahi, String> scriptParametersMap = (Map<ScriptParameterSahi, String>) parameterMap
				.get(Constants.WEB_SCRIPT_PARAMETERS_MAP);
		for (SeleniumParametersDTO dto : parametersDTOList) {
			List<Operation> operations = parseRegularCommand(dto, scriptParametersMap);
			operationList.addAll(operations);
		}
		return operationList;
	}

	public Map<String, Object> splitParameters(String[] args) {
		for (String s : args) {
			getLogger().info("Incoming argument: " + s);
		}
		Map<String, Object> parameterMap = new HashMap<>();
		List<SeleniumParametersDTO> parametersDTOList = new ArrayList<>();
		Map<ScriptParameterSahi, String> scriptParametersMap = new HashMap<>();
		for (String parameter : args) {
			getLogger().info("Incoming parameter: " + parameter);
			String[] commandKeyAndValue = parameter.split(Regex.EQUALS.getRegex());
			TestStepType operationType = TestStepType.fromValue(commandKeyAndValue[0]);
			getLogger().info("Command value: " + commandKeyAndValue[1]);
			if (operationType != null) {
				SeleniumParametersDTO dto = new SeleniumParametersDTO(operationType, commandKeyAndValue[1], testStepId);
				parametersDTOList.add(dto);
			} else {
				ScriptParameterSahi scriptParameterSahi = ScriptParameterSahi.fromName(commandKeyAndValue[0]);
				if (scriptParameterSahi != null) {
					scriptParametersMap.put(scriptParameterSahi, commandKeyAndValue[1]);
				}
			}
		}
		parameterMap.put(Constants.WEB_SCRIPT_PARAMETERS_DTO_LIST, parametersDTOList);
		parameterMap.put(Constants.WEB_SCRIPT_PARAMETERS_MAP, scriptParametersMap);
		return parameterMap;
	}

	private List<Operation> parseRegularCommand(SeleniumParametersDTO tempParameterDTO,
			Map<ScriptParameterSahi, String> scriptParametersMap) {
		SeleniumParametersDTO parametersDTO = findTestStepId(tempParameterDTO);
		List<Operation> operationList = new ArrayList<>();

		EnumMap<ScriptParameterSahi, Object> commandParameterMap = new EnumMap<>(ScriptParameterSahi.class);
		String[] parameterValueArray = parametersDTO.getStepTypeParameter()
				.split(Regex.INNER_PARAMETER_SPLITTER.getRegex());
		String[] innerParameterValueArray = parameterValueArray[0].split(Regex.PARAMETER_SPLITTER.getRegex());

		switch (parametersDTO.getTestStepType()) {
		case FORM_FILL:
			commandParameterMap.put(ScriptParameterSahi.UPLOADED_SNAPSHOT_ID, innerParameterValueArray[0]);
			commandParameterMap.put(ScriptParameterSahi.VERSION, scriptParametersMap.get(ScriptParameterSahi.VERSION));
			operationList = createWriteOperation(commandParameterMap, parametersDTO);
			break;
		case BUTTON_CLICK:
			if (parameterValueArray.length == 4) {
				commandParameterMap.put(ScriptParameterSahi.BUTTON_PARAMETER, parameterValueArray[3].substring(0,
						parameterValueArray[3].indexOf(Regex.PARAMETER_SPLITTER.getRegex())));
			} else {
				commandParameterMap.put(ScriptParameterSahi.BUTTON_PARAMETER, parameterValueArray[2].substring(0,
						parameterValueArray[2].indexOf(Regex.PARAMETER_SPLITTER.getRegex())));
			}
			commandParameterMap.put(ScriptParameterSahi.BUTTON_TYPE, parametersDTO.getStepTypeParameter());
			operationList = createClickOperation(commandParameterMap, parametersDTO);
			break;
		case VERIFY:
			commandParameterMap.put(ScriptParameterSahi.UPLOADED_SNAPSHOT_ID, innerParameterValueArray[0]);
			commandParameterMap.put(ScriptParameterSahi.VERSION, scriptParametersMap.get(ScriptParameterSahi.VERSION));
			operationList = createReadOperation(commandParameterMap, parametersDTO);
			break;
		case DOUBLE_CLICK:
			commandParameterMap.put(ScriptParameterSahi.BUTTON_PARAMETER,
					parameterValueArray[parameterValueArray.length - 1]);
			commandParameterMap.put(ScriptParameterSahi.BUTTON_TYPE, parametersDTO.getStepTypeParameter());
			operationList = createDoubleClickOperation(commandParameterMap, parametersDTO);
			break;
		case WAIT:
			commandParameterMap.put(ScriptParameterSahi.WAIT, parametersDTO.getStepTypeParameter());
			operationList = createWaitOperation(commandParameterMap, parametersDTO);
			break;
		default:
			break;
		}
		return operationList;
	}

	private List<Operation> createWriteOperation(EnumMap<ScriptParameterSahi, Object> commandParameterMap,
			SeleniumParametersDTO parametersDTO) {
		List<Operation> operationList = new ArrayList<>();
		Integer snapshotDefinitionId = Integer
				.valueOf((String) commandParameterMap.get(ScriptParameterSahi.UPLOADED_SNAPSHOT_ID));
		String version = (String) commandParameterMap.get(ScriptParameterSahi.VERSION);

		List<SnapshotValue> snapshotValueList = scriptService.getSnapshotFormFillValueBOList(version,
				snapshotDefinitionId);

		for (SnapshotValue snapshotValue : snapshotValueList) {
			SeleniumByType byType = SeleniumByType.findSeleniumByType(snapshotValue.getType());
			OperationParam param = new OperationParam(null, 0, byType, snapshotValue.getTag(), snapshotValue.getValue(),
					testStepId, parametersDTO.getTestStepType());
			Operation writeOperation = new WriteOperation(param);
			operationList.add(writeOperation);
		}
		return operationList;
	}

	private List<Operation> createClickOperation(EnumMap<ScriptParameterSahi, Object> commandParameterMap,
			SeleniumParametersDTO parametersDTO) {
		List<Operation> operationList = new ArrayList<>();
		String buttonTag = (String) commandParameterMap.get(ScriptParameterSahi.BUTTON_PARAMETER);
		String buttonType = (String) commandParameterMap.get(ScriptParameterSahi.BUTTON_TYPE);

		SeleniumByType byType = SeleniumByType.findSeleniumByType(buttonType);
		OperationParam param = new OperationParam(null, 0, byType, buttonTag, null, testStepId,
				parametersDTO.getTestStepType());
		Operation clickOperation = new ClickOperation(param);
		operationList.add(clickOperation);

		return operationList;
	}

	private List<Operation> createDoubleClickOperation(EnumMap<ScriptParameterSahi, Object> commandParameterMap,
			SeleniumParametersDTO parametersDTO) {
		List<Operation> operationList = new ArrayList<>();
		String buttonTag = (String) commandParameterMap.get(ScriptParameterSahi.BUTTON_PARAMETER);
		String buttonType = (String) commandParameterMap.get(ScriptParameterSahi.BUTTON_TYPE);

		SeleniumByType byType = SeleniumByType.findSeleniumByType(buttonType);
		OperationParam param = new OperationParam(null, 0, byType, buttonTag, null, testStepId,
				parametersDTO.getTestStepType());
		Operation clickOperation = new DoubleClickOperation(param);
		operationList.add(clickOperation);

		return operationList;
	}
	
	private List<Operation> createWaitOperation(EnumMap<ScriptParameterSahi, Object> commandParameterMap,
			SeleniumParametersDTO parametersDTO) {
		List<Operation> operationList = new ArrayList<>();
		String wait = (String) commandParameterMap.get(ScriptParameterSahi.WAIT);

		OperationParam param = new OperationParam(null, 0, null, ScriptParameterSahi.WAIT.name(), wait, testStepId,
				parametersDTO.getTestStepType());
		Operation waitOperation = new WaitOperation(param);
		operationList.add(waitOperation);
		return operationList;
	}

	private List<Operation> createReadOperation(EnumMap<ScriptParameterSahi, Object> commandParameterMap,
			SeleniumParametersDTO parametersDTO) {
		List<Operation> operationList = new ArrayList<>();
		Integer snapshotDefinitionId = Integer
				.valueOf((String) commandParameterMap.get(ScriptParameterSahi.UPLOADED_SNAPSHOT_ID));
		String version = (String) commandParameterMap.get(ScriptParameterSahi.VERSION);

		List<SnapshotValue> snapshotValueList = scriptService.getSnapshotFormFillValueBOList(version,
				snapshotDefinitionId);

		for (SnapshotValue snapshotValue : snapshotValueList) {
			SeleniumByType byType = SeleniumByType.findSeleniumByType(snapshotValue.getType());
			OperationParam param = new OperationParam(null, 0, byType, snapshotValue.getTag(), snapshotValue.getValue(),
					testStepId, parametersDTO.getTestStepType());
			Operation readOperation = new ReadOperation(param);
			operationList.add(readOperation);
		}

		return operationList;
	}

	private SeleniumParametersDTO findTestStepId(SeleniumParametersDTO parametersDTO) {
		SeleniumParametersDTO tempParametersDTO = parametersDTO;
		tempParametersDTO.setStepTypeParameter(
				parametersDTO.getStepTypeParameter().replaceAll(Regex.SPACE.getRegex(), Constants.TEXT_BLANK));
		tempParametersDTO.setStepTypeParameter(changeSpecialCharacterInParameter(parametersDTO.getStepTypeParameter()));
		String commandValue = tempParametersDTO.getStepTypeParameter();
		if (commandValue.indexOf(Regex.TEST_STEP.getRegex()) != -1) {
			if (commandValue.indexOf(Regex.FORM_NAME.getRegex()) != -1) {
				testStepId = Integer.valueOf(commandValue.substring(
						commandValue.indexOf(Regex.TEST_STEP.getRegex()) + Regex.TEST_STEP.getRegex().length(),
						commandValue.indexOf(Regex.FORM_NAME.getRegex())));
			} else {
				testStepId = Integer.valueOf(commandValue.substring(
						commandValue.indexOf(Regex.TEST_STEP.getRegex()) + Regex.TEST_STEP.getRegex().length()));
			}
			tempParametersDTO.setTestStepId(testStepId);
		}
		commandValue = tempParametersDTO.getStepTypeParameter();
		getLogger().info("Command: " + commandValue);
		tempParametersDTO
				.setStepTypeParameter(commandValue.substring(0, commandValue.indexOf(Regex.TEST_STEP.getRegex())));
		return tempParametersDTO;
	}

	private String changeSpecialCharacterInParameter(String parameter) {
		if (parameter.contains(Constants.TEXT_AND_SCRIPT)) {
			return parameter.replace(Constants.TEXT_AND_SCRIPT, Constants.TEXT_AND);
		}
		return parameter;
	}

}
