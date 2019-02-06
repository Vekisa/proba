package hotelInterf;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.isap.ISAProject.model.hotel.ExtraOption;
import com.isap.ISAProject.model.hotel.Hotel;
import com.isap.ISAProject.model.hotel.RoomReservation;

public interface ExtraOptionServiceInterface {

	ExtraOption findById(long id);
	
	List<ExtraOption> findAll(Pageable pageable);
	
	ExtraOption save(ExtraOption extraOption);
	
	void deleteById(long id);
	
	ExtraOption updateExtraOptionById(Long extraOptionId, ExtraOption newExtraOption);
	
	List<RoomReservation> getRoomReservation(Long extraOptionId);
	
	Hotel getHotel(Long extraOptionId);
}
