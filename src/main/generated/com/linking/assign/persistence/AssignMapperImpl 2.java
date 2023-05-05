package com.linking.assign.persistence;

import com.linking.assign.domain.Assign;
import com.linking.assign.dto.AssignRes;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-05-02T13:29:46+0900",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 11.0.16.1 (Oracle Corporation)"
)
@Component
public class AssignMapperImpl implements AssignMapper {

    @Override
    public List<AssignRes> toDto(List<Assign> assignList) {
        if ( assignList == null ) {
            return null;
        }

        List<AssignRes> list = new ArrayList<AssignRes>( assignList.size() );
        for ( Assign assign : assignList ) {
            list.add( toDto( assign ) );
        }

        return list;
    }
}
