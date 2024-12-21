package com.zahid.socks_api.services;

import com.zahid.socks_api.SocksApiApplication;
import com.zahid.socks_api.entity.SocksBatch;
import com.zahid.socks_api.exceptions.LackOfSocksException;
import com.zahid.socks_api.exceptions.SocksDataException;
import com.zahid.socks_api.repositories.SocksRepository;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SocksService {
    private final SocksRepository socksRepository;
    @Autowired
    public SocksService(SocksRepository socksRepository) {
        this.socksRepository = socksRepository;
    }
    protected Optional<SocksBatch> getAvailableProduct(String color, int cotton) {
        return socksRepository.findSocksBatchByColorAndCotton(color, cotton);
    }

    public void add(SocksBatch income) {
        Optional<SocksBatch> availableSocks =
                getAvailableProduct(income.getColor(), income.getCotton());
        if (availableSocks.isEmpty()) socksRepository.save(income);
        else {
            SocksBatch socks = availableSocks.get();
            socks.setQuantity(socks.getQuantity() + income.getQuantity());
            socksRepository.save(socks);
        }
    }

    public void delete(SocksBatch outcome) {
        Optional<SocksBatch> availableSocks =
                getAvailableProduct(outcome.getColor(), outcome.getCotton());
        int requested = outcome.getQuantity();
        if (availableSocks.isEmpty()) throw new LackOfSocksException(0, requested);
        int available = availableSocks.map(SocksBatch::getQuantity).orElse(0);
        if (requested > available) throw new LackOfSocksException(available, requested);
        else if (requested == available) socksRepository.delete(availableSocks.get());
        else {
            SocksBatch socks = availableSocks.get();
            socks.setQuantity(available - requested);
            socksRepository.save(socks);
        }
    }

    public int count(String color, Integer min, Integer number, Integer max) {
        if ((min != null || max != null) && number != null)
            throw new SocksDataException("You should not enter both the exact value and the range boundaries at the same time. The request doesn't make sense");
        List<SocksBatch> socksBatches = socksRepository.findAll();
        if (color != null)
            socksBatches = socksBatches.stream().filter(x -> x.getColor().equals(color)).toList();
        if (number != null)
            socksBatches = socksBatches.stream().filter(x -> x.getCotton() == number).toList();
        else {
            if (min != null)
                socksBatches = socksBatches.stream().filter(x -> x.getCotton() >= min).toList();
            if (max != null)
                socksBatches = socksBatches.stream().filter(x -> x.getCotton() <= max).toList();
        }
        SocksApiApplication.logger.info("Quantity has been showed");
        return socksBatches.stream().mapToInt(SocksBatch::getQuantity).sum();
    }

    public void change(SocksBatch newValue, int id) {
        Optional<SocksBatch> currentValueOptional = socksRepository.findById(id);
        if (currentValueOptional.isEmpty()) throw new SocksDataException("Not found socks batch with id " + id);
        SocksBatch oldValue = currentValueOptional.get();
        if (newValue.getColor() != null) oldValue.setColor(newValue.getColor());
        if (newValue.getCotton() != 0) oldValue.setCotton((newValue.getCotton()));
        if (newValue.getQuantity() != 0) oldValue.setQuantity(newValue.getQuantity());
        Optional<SocksBatch> sameFeaturesSocksBatchOptional = getAvailableProduct(newValue.getColor(), newValue.getCotton());
        if (sameFeaturesSocksBatchOptional.isPresent()) {
            SocksBatch sameBatch = sameFeaturesSocksBatchOptional.get();
            oldValue.setQuantity(oldValue.getQuantity() + sameBatch.getQuantity());
            socksRepository.delete(sameBatch);
        }
        socksRepository.save(oldValue);
    }

    public void parseExcel(MultipartFile file) throws IOException {
        Workbook workbook = new XSSFWorkbook(file.getInputStream());
        Sheet sheet = workbook.getSheetAt(0); // suppose data located at first sheet
        List<SocksBatch> sockBatches = new ArrayList<>();
        for (Row row : sheet) {
            if (row.getRowNum() == 0) continue; // skip table header
            String color = row.getCell(0).getStringCellValue();
            int cottonPercent = (int) row.getCell(1).getNumericCellValue();
            int quantity = (int) row.getCell(2).getNumericCellValue();
            sockBatches.add(new SocksBatch(color, cottonPercent, quantity));
        }
        sockBatches.forEach(this::add);
        workbook.close();
    }

    public List<SocksBatch> searchWithFilter(String sortCriteria, Integer min, Integer max) {
        List<SocksBatch> socksBatches = socksRepository.findAll();
        if (min != null) socksBatches = socksBatches.stream()
                .filter(x -> x.getCotton() >= min).collect(Collectors.toCollection(ArrayList::new));
        if (max != null) socksBatches = socksBatches.stream()
                    .filter(x -> x.getCotton() <= max).collect(Collectors.toCollection(ArrayList::new));
        if (sortCriteria != null) {
            switch (sortCriteria) {
                case "color" -> socksBatches.sort(Comparator.comparing(SocksBatch::getColor));
                case "cotton" -> socksBatches.sort(Comparator.comparingInt(SocksBatch::getCotton));
                default -> throw new RuntimeException("Wrong sort criteria");
            }
        }
        SocksApiApplication.logger.info("Socks batches list has been showed");
        return socksBatches;
    }

}
