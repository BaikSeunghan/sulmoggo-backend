package com.hanghae99.sulmocco.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.hanghae99.sulmocco.model.TableImage;
import com.hanghae99.sulmocco.model.Tables;
import lombok.*;
import org.springframework.data.domain.Slice;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)  //null인 데이터는 json 결과에 나오지 않음
public class TablesResponseDto {

    private Long tableId;
    private String title;
    private String username;
    private String content;
    private String thumbnail;
    private List<String> imgUrlList;
    private String alcoholtag;
    private String freetag;
    private Integer likecount;
    private Integer viewcount;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private boolean isBookmark;
    private boolean isLike;
    private String profileimgurl;

    private String level;

    private List<ReplyResponseDto> replies;

    // 술상 추천 목록, 본인이 작성한 술상 목록
    public TablesResponseDto(Tables tables) {
        // todo : total ? / lastPage ?
        this.tableId = tables.getId();
        this.title = tables.getTitle();
        this.username = tables.getUser().getUsername();
//        this.thumbnail = tables.getThumbnail().getThumbnailUrl();
        this.thumbnail = tables.getThumbnailImgUrl();
        this.likecount = tables.getLikes().size();
        this.viewcount = tables.getViewUserList().size();
        this.alcoholtag = tables.getAlcoholTag();
        this.freetag = tables.getFreeTag();
        this.createdAt = tables.getCreatedAt();
        this.level = tables.getUser().getLevel();
        this.profileimgurl = tables.getUser().getProfileUrl();
    }

    // 술상 추천 상세
    public TablesResponseDto(Tables tables, List<ReplyResponseDto> replies,
                             Boolean isBookmark, Boolean isLike) {
        this.tableId = tables.getId();
        this.title = tables.getTitle();
        this.username = tables.getUser().getUsername();
        this.content = tables.getContent();
        //        this.thumbnail = tables.getThumbnail().getThumbnailUrl();
        this.thumbnail = tables.getThumbnailImgUrl();
        this.imgUrlList = tables.getImgUrls().stream()
                .map(this::apply)
                .collect(Collectors.toList());
        this.replies = replies; // findTable.getReplies(); // 이걸로도 되나 궁금하다
        this.alcoholtag = tables.getAlcoholTag();
        this.freetag = tables.getFreeTag();
        this.likecount = tables.getLikes().size();
        this.viewcount = tables.getViewUserList().size();
        this.isBookmark = isBookmark;
        this.isLike = isLike;
        this.createdAt = tables.getCreatedAt();
        this.profileimgurl = tables.getUser().getProfileUrl();
        this.modifiedAt = tables.getModifiedAt();
    }

    private String apply(TableImage tableImage) {
        return tableImage.getTableImgUrl();
    }

    // 오늘의 술상 추천
    public static TablesResponseDto todayTableDto(Tables tables) {
        return TablesResponseDto.builder()
                .tableId(tables.getId())
//                .thumbnail(tables.getThumbnail().getThumbnailUrl())
                .thumbnail(tables.getThumbnailImgUrl())
                .title(tables.getTitle())
                .likecount(tables.getLikes().size())
                .viewcount(tables.getViewUserList().size())
                .alcoholtag(tables.getAlcoholTag())
                .freetag(tables.getFreeTag())
                .profileimgurl(tables.getUser().getProfileUrl())
                .username(tables.getUser().getUsername())
                .level(tables.getUser().getLevel())
                .createdAt(tables.getCreatedAt())
                .build();
    }

    // 본인이 작성한 술상 목록
    public static Slice<TablesResponseDto> myPageTablesResponseDto(Slice<Tables> tableSlice) {
        Slice<TablesResponseDto> tablesResponseDtoSlice = tableSlice.map(t ->
                        TablesResponseDto.builder()
                                .tableId(t.getId())
                                .username(t.getUser().getUsername())
                                .title(t.getTitle())
                                .content(t.getContent())
//                                .thumbnail(p.getThumbnail().getThumbnailUrl())
                                .thumbnail(t.getThumbnailImgUrl())
                                .likecount(t.getLikes().size())
                                .viewcount(t.getViewUserList().size())
                                .alcoholtag(t.getAlcoholTag())
                                .freetag(t.getFreeTag())
                                .createdAt(t.getCreatedAt())
                                .profileimgurl(t.getUser().getProfileUrl())
                                .build()
        );
        return tablesResponseDtoSlice;
    }
}
