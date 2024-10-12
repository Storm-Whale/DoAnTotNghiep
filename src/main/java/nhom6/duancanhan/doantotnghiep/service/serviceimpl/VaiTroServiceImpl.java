package nhom6.duancanhan.doantotnghiep.service.serviceimpl;

import nhom6.duancanhan.doantotnghiep.entity.VaiTro;
import nhom6.duancanhan.doantotnghiep.repository.VaiTroRepository;
import nhom6.duancanhan.doantotnghiep.service.service.VaiTroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VaiTroServiceImpl implements VaiTroService {
    @Autowired
    private VaiTroRepository vaiTroRepository;
    @Override
    public List<VaiTro> getAll() {
        return vaiTroRepository.findAll();
    }
}
