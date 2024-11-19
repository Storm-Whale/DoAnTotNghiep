package nhom6.duancanhan.doantotnghiep.service.service;

import nhom6.duancanhan.doantotnghiep.dto.AnhSanPhamRequest;
import nhom6.duancanhan.doantotnghiep.dto.AnhSanPhamResponse;

import java.util.List;

public interface AnhSanPhamService {

    AnhSanPhamResponse storeAnhSanPham(AnhSanPhamRequest anhSanPhamRequest);

    List<AnhSanPhamResponse> getAnhSanPhamByIdSPCT(Integer idSpct);

    AnhSanPhamResponse updateAnhSanPham(Integer id, AnhSanPhamRequest anhSanPhamRequest);

    AnhSanPhamResponse getAnhSanPhamById(Integer id);
}
