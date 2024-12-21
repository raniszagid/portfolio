package com.zahid.socks_api.controllers;

import com.zahid.socks_api.dto.SocksDto;
import com.zahid.socks_api.entity.SocksBatch;
import com.zahid.socks_api.exceptions.SocksDataException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.multipart.MultipartFile;
import static org.junit.jupiter.api.Assertions.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

@SpringBootTest
public class SocksControllerTests {
    private final SocksController controller;
    @Autowired
    public SocksControllerTests(SocksController controller) {
        this.controller = controller;
    }

    @Test
    public void test_convertToEntity() {
        String color = "orange";
        int cotton = 88;
        int quantity = 23;
        SocksDto dto = new SocksDto(color, cotton, quantity);
        SocksBatch entity = controller.convertToEntity(dto);
        assertEquals(color, entity.getColor());
        assertEquals(cotton, entity.getCotton());
        assertEquals(quantity, entity.getQuantity());
    }

    @Test
    public void test_add() {
        SocksDto nullColorData = new SocksDto(null, 2,3);
        SocksDto wrongCottonValueData = new SocksDto("pink", 150, 4);
        SocksDto negativeQuantityData = new SocksDto("purple", 34, -54);
        Executable executable = () -> controller.add(nullColorData);
        assertThrows(SocksDataException.class, executable);
        executable = () -> controller.add(wrongCottonValueData);
        assertThrows(SocksDataException.class, executable);
        executable = () -> controller.add(negativeQuantityData);
        assertThrows(SocksDataException.class, executable);
    }

    @Test
    public void uploadExcel_WrongNameFile_ThrowsSocksDataException() {
        MultipartFile wrongNameFile = new MultipartFile() {
            @Override
            public String getName() {
                return null;
            }
            @Override
            public String getOriginalFilename() {
                return "xxxxxxx";
            }
            @Override
            public String getContentType() {
                return null;
            }
            @Override
            public boolean isEmpty() {
                return false;
            }
            @Override
            public long getSize() {
                return 0;
            }
            @Override
            public byte[] getBytes() throws IOException { return new byte[0]; }
            @Override
            public InputStream getInputStream() throws IOException {return null;}
            @Override
            public void transferTo(File dest) throws IOException, IllegalStateException {            }
        };
        Executable executable = () -> controller.uploadExcel(wrongNameFile);
        assertThrows(SocksDataException.class, executable);
    }
}
