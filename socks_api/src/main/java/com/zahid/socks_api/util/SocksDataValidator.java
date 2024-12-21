package com.zahid.socks_api.util;

import com.zahid.socks_api.dto.SocksDto;
import com.zahid.socks_api.exceptions.SocksDataException;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class SocksDataValidator {
    public void checkInputData(SocksDto data, boolean necessity) {
        if (necessity) {
            if (data.getColor() == null) {
                throw new SocksDataException("Color cannot be empty");
            }
            if (data.getCotton() <= 0 || data.getCotton() > 100) {
                throw new SocksDataException("Cotton percentage value must be more than 0 and less" +
                        " (or equal) than 100, while your value is " + data.getCotton());
                }
            if (data.getQuantity() <= 0)
                throw new SocksDataException("Quantity of socks pairs in the batch must be positive value");
        } else {
            if (data.getCotton() < 0 || data.getCotton() > 100)
                throw new SocksDataException("Wrong cotton value");
            if (data.getQuantity() < 0)
                throw new SocksDataException("Quantity of socks pairs in the batch must be positive value");
        }
    }

    protected boolean checkCottonValues(Integer number) {
        if (number == null) return true;
        return (number >= 0 && number <= 100);
    }

    public void checkCountingParameters(Integer min, Integer max, Integer number) {
        if (!checkCottonValues(min) || !checkCottonValues(max) || !checkCottonValues(number))
            throw new SocksDataException("Cotton percentage values must be between 0 and 100");
        if (min != null && max != null) {
            if (max < min)
                throw new SocksDataException("The end of interval is less than its start number");
        }
    }

    public void checkFilterParameters(Integer min, Integer max) {
        if (!checkCottonValues(min) || !checkCottonValues(max))
            throw new SocksDataException("Cotton percentage values must be between 0 and 100");
        if (min != null && max != null) {
            if (max < min)
                throw new SocksDataException("The end of interval is less than its start number");
        }
    }

    public void checkFile(MultipartFile file) {
        if (!file.getOriginalFilename().endsWith(".xlsx"))
            throw new SocksDataException("Wrong file format. Needed .xlsx");
    }
}
