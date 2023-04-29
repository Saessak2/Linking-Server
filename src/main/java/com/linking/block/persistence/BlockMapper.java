package com.linking.block.persistence;

import com.linking.annotation.dto.AnnotationRes;
import com.linking.block.domain.Block;
import com.linking.block.dto.BlockCreateReq;
import com.linking.block.dto.BlockDetailRes;
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

    default BlockDetailRes toDto(Block source, List<AnnotationRes> annotationResList) {

        BlockDetailRes.BlockDetailResBuilder builder = BlockDetailRes.builder();
        builder
                .blockId(source.getId())
                .pageId(source.getPage().getId())
                .title(source.getTitle())
                .content(source.getContent())
                .annotationResList(annotationResList);

        return builder.build();
    }

    default BlockRes toDto(Block source) {

        BlockRes.BlockResBuilder builder = BlockRes.builder();
        builder
                .blockId(source.getId())
                .pageId(source.getPage().getId())
                .title(source.getTitle())
                .content(source.getContent());
//                .annotationResList(annotationResList);

        return builder.build();
    }


    default List<BlockDetailRes> toDummyDto() {
        List<BlockDetailRes> blockResList = new ArrayList<>();
        List<AnnotationRes> annotationResList = new ArrayList<>();

        AnnotationRes annotationRes = AnnotationRes.builder()
                .annotationId(-1L)
                .blockId(-1L)
                .content("")
                .lastModified("00-00-00 AM 00:00")
                .userName("")
                .build();
        annotationResList.add(annotationRes);

        BlockDetailRes blockRes = BlockDetailRes.builder()
                .blockId(-1L)
                .pageId(-1L)
                .title("")
                .annotationResList(annotationResList)
                .build();
        blockResList.add(blockRes);

        return blockResList;
    }

    default Block toEntity(BlockCreateReq source) {

        Block.BlockBuilder builder = Block.builder();
        builder
                .title(source.getTitle())
                .blockOrder(source.getOrder());

        return builder.build();
    }
}
