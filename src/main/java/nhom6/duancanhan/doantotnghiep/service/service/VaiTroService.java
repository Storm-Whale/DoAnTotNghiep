package nhom6.duancanhan.doantotnghiep.service.service;

import nhom6.duancanhan.doantotnghiep.entity.VaiTro;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface VaiTroService{
    List<VaiTro> getAll();
}
