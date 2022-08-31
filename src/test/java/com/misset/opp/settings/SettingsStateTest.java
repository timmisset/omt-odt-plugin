package com.misset.opp.settings;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class SettingsStateTest {
    SettingsState settingsState;

    @BeforeEach
    void setUp() {
        settingsState = new SettingsState();
        settingsState.setTsConfigPath(Paths.get("src", "test", "resources", "tsconfig.base.json").toAbsolutePath().toString());
    }

    @Test
    void testGetMappingPath() {
        String mappingPath = settingsState.getMappingPath("@domainA/fileA.omt");
        mappingPath = mappingPath.replaceAll("\\\\", "/");
        assertTrue(mappingPath.endsWith("someLocation/domain/A/fileA.omt"));
    }

    @Test
    void testGetMappingPathSubfolder() {
        String mappingPath = settingsState.getMappingPath("@domainA/subfolder/fileA.omt");
        mappingPath = mappingPath.replaceAll("\\\\", "/");
        assertTrue(mappingPath.endsWith("someLocation/domain/A/subfolder/fileA.omt"));
    }

    @Test
    void testGetShorthandPathRoot() {
        String fullPath = Path.of("src/test/resources/someLocation/domain/A/fileA.omt").toAbsolutePath().toString();
        assertEquals(Path.of("@domainA/fileA.omt").toString(), settingsState.getShorthandPath(fullPath));
    }

    @Test
    void testGetShorthandPathSubfolder() {
        String fullPath = Path.of("src/test/resources/someLocation/domain/B/another/folder/or/subfolder/fileB.omt").toAbsolutePath().toString();
        assertEquals(Path.of("@domainB/another/folder/or/subfolder/fileB.omt").toString(), settingsState.getShorthandPath(fullPath));
    }

    @Test
    void testGetMappingPathReturnsNullWhenNotFound() {
        assertNull(settingsState.getMappingPath("@domainC/subfolder/fileA.omt"));
    }
}
