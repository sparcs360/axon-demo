package com.sparcs.spring;

import java.util.Arrays;

import org.springframework.core.env.Environment;

public class EnvironmentUtils {

	public static boolean isProfileActive(Environment environment, String profileName) {

		return Arrays.stream(environment.getActiveProfiles()).anyMatch(activeProfileName -> activeProfileName.equals(profileName));
	}
}
