package nhom6.duancanhan.doantotnghiep.service.service;

import nhom6.duancanhan.doantotnghiep.dto.AnhSanPhamRequest;
import nhom6.duancanhan.doantotnghiep.dto.AnhSanPhamResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AnhSanPhamService {

    AnhSanPhamResponse storeAnhSanPham(AnhSanPhamRequest anhSanPhamRequest);

    List<AnhSanPhamResponse> getAnhSanPhamByIdSPCT(Integer idSpct);
}
