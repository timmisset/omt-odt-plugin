package com.misset.opp.documentation;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class ApiDocumentationServiceTest {

    @Test
    void testThrowsRuntimeExceptionWhenFileDoesntExist() {
        ApiDocumentationService documentationService = new ApiDocumentationService(null) {
            @Override
            protected @NotNull Path getPathToApiDocument() {
                return Path.of("/wrong/path");
            }
        };

        assertThrows(RuntimeException.class, () -> documentationService.readApiDocumentation("/some/Path"));
    }

    @Test
    void testIOExceptionIsCaughtAndReturnsNull() {
        Path path = mock(Path.class, RETURNS_DEEP_STUBS);
        when(path.toFile().exists()).thenReturn(true);
        ApiDocumentationService documentationService = new ApiDocumentationService(null) {
            @Override
            protected @NotNull Path getPathToApiDocument() {
                return path;
            }
        };

        assertNull(documentationService.readApiDocumentation("/some/path"));
    }
}
