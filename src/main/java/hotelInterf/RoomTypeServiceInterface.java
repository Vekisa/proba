package hotelInterf;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.isap.ISAProject.model.hotel.Catalogue;
import com.isap.ISAProject.model.hotel.RoomType;

public interface RoomTypeServiceInterface {

	RoomType findById(long id);
	
	List<RoomType> findAll(Pageable pageable);
	
	RoomType save(RoomType roomType);
	
	void deleteById(long id);
	
	RoomType updateRoomTypeById(Long roomTypeId, RoomType newRoomType);
	
	Catalogue getCatalogue(Long roomTypeId);
}
