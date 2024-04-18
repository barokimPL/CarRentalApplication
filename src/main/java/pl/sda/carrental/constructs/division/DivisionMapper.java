package pl.sda.carrental.constructs.division;

import java.util.ArrayList;
import java.util.List;

public class DivisionMapper {

    public static DivisionDTO map(Division division){
        DivisionDTO DivisionDto = new DivisionDTO();
        DivisionDto.setDivision_id(division.getDivision_id());
        DivisionDto.setAddress(division.getAddress());
        return DivisionDto;
    }

    public static Division map(DivisionDTO DivisionDto){
        Division Division = new Division();
        Division.setDivision_id(DivisionDto.getDivision_id());
        Division.setAddress(DivisionDto.getAddress());
        return Division;
    }

    public static List<DivisionDTO> mapEntityListToDtoList(List<Division> divisions){

        List<DivisionDTO> result = new ArrayList<>();
        for (Division division: divisions) {
            result.add(map(division));
        }
        return result;
    }

    public static List<Division> mapDtoListToEntityList(List<DivisionDTO> dtos){

        List<Division> result = new ArrayList<>();
        for (DivisionDTO dto: dtos) {
            result.add(map(dto));
        }
        return result;
    }
}
