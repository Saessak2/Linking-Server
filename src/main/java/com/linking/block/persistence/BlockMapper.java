package com.linking.block.persistence;


import com.linking.annotation.persistence.AnnotationMapper;
import com.linking.block.domain.Block;
import com.linking.block.dto.BlockCreateReq;
import com.linking.block.dto.BlockRes;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface BlockMapper {

    default BlockRes toDto(Block source) {
        if (source == null) {
            return null;
        }
        BlockRes.BlockResBuilder builder = BlockRes.builder();
        builder
                .blockId(source.getId())
                .blockIndex(source.getBlockIndex())
                .title(source.getTitle())
                .pageId(source.getPage().getId())
                .content(source.getContent());
//        if (!source.getAnnotationList().isEmpty()) {
//            builder.annotationResList(
//                    source.getAnnotationList()
//                            .stream(v -> AnnotationMapper.toDto)
//            );
//        }
        return builder.build();
    }

    default Block toEntity(BlockCreateReq source) {
        if (source == null) {
            return null;
        }
        Block.BlockBuilder builder = Block.builder();
        builder
                .blockIndex(source.getBlockIndex());

        return builder.build();
    }
}
