package com.linking.block.persistence;

import com.linking.annotation.dto.AnnotationRes;
import com.linking.block.domain.Block;
import com.linking.block.dto.BlockCreateReq;
import com.linking.block.dto.BlockRes;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.ArrayList;
import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface BlockMapper {

    default BlockRes toDto(Block source, List<AnnotationRes> annotationResList) {
        if (source == null) return null;

        BlockRes.BlockResBuilder builder = BlockRes.builder();
        builder
                .blockId(source.getId())
                .title(source.getTitle())
                .pageId(source.getPage().getId())
                .order(source.getBlockOrder())
                .annotationResList(annotationResList);

        return builder.build();
    }

    default Block toEntity(BlockCreateReq source) {
        if (source == null) {
            return null;
        }
        Block.BlockBuilder builder = Block.builder();
        builder
                .title(source.getTitle())
                .blockOrder(source.getOrder());

        return builder.build();
    }


}
