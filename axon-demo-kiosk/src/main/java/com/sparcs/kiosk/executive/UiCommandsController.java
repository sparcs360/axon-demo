package com.sparcs.kiosk.executive;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.NestedRuntimeException;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparcs.kiosk.IEndUserCommand;
import com.sparcs.kiosk.IKioskCommand;
import com.sparcs.kiosk.config.KioskProperties;

@Controller
public class UiCommandsController {

	private static final Logger LOG = LoggerFactory.getLogger(UiCommandsController.class);

	private static final TypeReference<HashMap<String, Object>> TYPEREF_MAP = new TypeReference<HashMap<String, Object>>() {};

	private final KioskProperties kioskProperties;
	private final CommandGateway commandGateway;
	private final ObjectMapper objectMapper;

	@Autowired
	public UiCommandsController(KioskProperties kioskProperties, CommandGateway commandGateway) {

		this.kioskProperties = kioskProperties;
		this.commandGateway = commandGateway;

		objectMapper = new ObjectMapper();
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);
	}

	@MessageMapping("/send/{name}")
	public void sendCommand(@DestinationVariable String name, String payload) {

		LOG.debug("sendCommand(name={}, payload={})", name, payload);
		
		Map<String, Object> payloadAsMap = null;
		Class<? extends IEndUserCommand> commandClass = null;
		IKioskCommand command = null;

		try {
			payloadAsMap = addKioskIdToPayload(payload);
		} catch (IOException e) {
			InvalidCommandException customException = new InvalidCommandException("Failed to deserialise payload (most likely a JSON syntax error)", e);
			LOG.error(customException.getMessage());
			throw customException;
		}

		try {
			commandClass = findCommand(name);
		} catch (Exception e) {
			InvalidCommandException customException = new InvalidCommandException(String.format("'%s' is not a recognised command name", name));
			LOG.error(customException.getMessage());
			throw customException;
		}

		try {
			command = objectMapper.convertValue(payloadAsMap, commandClass);
			LOG.trace("command={}", command);

		} catch (Exception e) {
			InvalidCommandException customException = new InvalidCommandException("Failed to serialise command", e);
			LOG.error(customException.getMessage());
			throw customException;
		}

		commandGateway.send(command);
	}

	private Map<String, Object> addKioskIdToPayload(String payload) throws IOException {

		Map<String, Object> asMap;
		if (StringUtils.hasLength(payload)) {
			asMap = objectMapper.readValue(payload, TYPEREF_MAP);
		} else {
			asMap = new HashMap<>();
		}
		asMap.put("kioskId", kioskProperties.getKioskId());
		LOG.trace("asMap={}", asMap);
		return asMap;
	}

	private Class<? extends IEndUserCommand> findCommand(String name) throws ClassNotFoundException {

		String fullClassName = String.format("%s.%s", IEndUserCommand.class.getPackage().getName(), name);
		@SuppressWarnings("unchecked")
		Class<? extends IEndUserCommand> commandClass = (Class<? extends IEndUserCommand>) Class.forName(fullClassName);
		LOG.trace("commandClass={}", commandClass);
		return commandClass;
	}
	
	public static class InvalidCommandException extends NestedRuntimeException {

		private static final long serialVersionUID = 1L;

		public InvalidCommandException(String msg) {
			super(msg);
		}

		public InvalidCommandException(String msg, Throwable cause) {
			super(msg, cause);
		}
	}
}
