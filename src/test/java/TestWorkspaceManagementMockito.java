import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

class TestWorkspaceManagementMockito{

    private WorkspaceManagement workspaceManagement;
    private CoworkingSpace mockSpace1;
    private CoworkingSpace mockSpace2;

    @BeforeEach
    void setUp() {
        workspaceManagement = new WorkspaceManagement();

        // Мокаем CoworkingSpace
        mockSpace1 = Mockito.mock(CoworkingSpace.class);
        mockSpace2 = Mockito.mock(CoworkingSpace.class);

        // Указываем поведение моков
        when(mockSpace1.getSpaceId()).thenReturn(1);
        when(mockSpace1.getType()).thenReturn("Private Office");
        when(mockSpace1.getPrice()).thenReturn(100.0);
        when(mockSpace1.isAvailable()).thenReturn(true);

        when(mockSpace2.getSpaceId()).thenReturn(2);
        when(mockSpace2.getType()).thenReturn("Open Space");
        when(mockSpace2.getPrice()).thenReturn(50.0);
        when(mockSpace2.isAvailable()).thenReturn(false);

        // Добавляем в список
        List<CoworkingSpace> spaces = new ArrayList<>();
        spaces.add(mockSpace1);
        spaces.add(mockSpace2);

        // Устанавливаем список в WorkspaceManagement
        workspaceManagement.setCoworkingSpaces(spaces);
    }

    @Test
    void testSaveSpacesToFile() throws IOException {
        // Перенаправляем System.out в буфер, чтобы проверить вывод
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        // Запускаем метод
        workspaceManagement.saveSpacesToFile();

        // Проверяем, что файл был создан
        File file = new File("src/main/java/spaces.txt");
        assert file.exists() : "Файл spaces.txt не был создан!";

        // Проверяем, что метод вывел успешное сообщение
        String output = outputStream.toString().trim();
        assert output.contains("Coworking spaces saved successfully.") : "Сообщение об успешном сохранении не найдено!";
    }
}

