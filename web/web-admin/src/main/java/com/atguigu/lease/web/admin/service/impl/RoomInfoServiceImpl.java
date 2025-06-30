package com.atguigu.lease.web.admin.service.impl;

import com.atguigu.lease.model.entity.*;
import com.atguigu.lease.model.enums.ItemType;
import com.atguigu.lease.web.admin.mapper.*;
import com.atguigu.lease.web.admin.service.*;
import com.atguigu.lease.web.admin.vo.attr.AttrValueVo;
import com.atguigu.lease.web.admin.vo.graph.GraphVo;
import com.atguigu.lease.web.admin.vo.room.RoomDetailVo;
import com.atguigu.lease.web.admin.vo.room.RoomItemVo;
import com.atguigu.lease.web.admin.vo.room.RoomQueryVo;
import com.atguigu.lease.web.admin.vo.room.RoomSubmitVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liubo
 * @description 针对表【room_info(房间信息表)】的数据库操作Service实现
 * @createDate 2023-07-24 15:48:00
 */
@Service
public class RoomInfoServiceImpl extends ServiceImpl<RoomInfoMapper, RoomInfo>
        implements RoomInfoService {

    @Autowired
    GraphInfoService graphInfoService;
    @Autowired
    private RoomAttrValueService roomAttrValueService;
    @Autowired
    private RoomFacilityService roomFacilityService;
    @Autowired
    private RoomLabelService roomLabelService;
    @Autowired
    private RoomPaymentTypeService paymentTypeService;
    @Autowired
    private RoomLeaseTermService roomLeaseTermService;

    @Autowired
    private RoomInfoMapper roomInfoMapper;
    @Autowired
    private ApartmentInfoMapper apartmentInfoMapper;

    @Autowired
    private GraphInfoMapper graphInfoMapper;
    @Autowired
    private AttrValueMapper attrValueMapper;
    @Autowired
    private FacilityInfoMapper facilityInfoMapper;
    @Autowired
    private LabelInfoMapper labelInfoMapper;
    @Autowired
    private PaymentTypeMapper paymentTypeMapper;
    @Autowired
    private LeaseTermMapper leaseTermMapper;
    @Override
    public void saveOrUpdateRoomSubmitVo(RoomSubmitVo roomSubmitVo) {
        //
        boolean update = roomSubmitVo.getId() != null;
        super.saveOrUpdate(roomSubmitVo);
        if(update){
            //删除图片信息
            LambdaQueryWrapper<GraphInfo> graphQueryWrapper = new LambdaQueryWrapper<>();
            graphQueryWrapper.eq(GraphInfo::getItemType, ItemType.ROOM);
            graphQueryWrapper.eq(GraphInfo::getItemId, roomSubmitVo.getId());
            graphInfoService.remove(graphQueryWrapper);
            //删除属性信息列表
            LambdaQueryWrapper<RoomAttrValue> roomQueryWrapper = new LambdaQueryWrapper<>();
            roomQueryWrapper.eq(RoomAttrValue::getRoomId, roomSubmitVo.getId());
            roomAttrValueService.remove(roomQueryWrapper);
            //删除配套信息列表
            LambdaQueryWrapper<RoomFacility> roomFacilityQueryWrapper = new LambdaQueryWrapper<>();
            roomFacilityQueryWrapper.eq(RoomFacility::getRoomId,roomSubmitVo.getId());
            roomFacilityService.remove(roomFacilityQueryWrapper);
            //删除标签列表
            LambdaQueryWrapper<RoomLabel> roomLabelQueryWrapper = new LambdaQueryWrapper<>();
            roomLabelQueryWrapper.eq(RoomLabel::getRoomId,roomSubmitVo.getId());
            roomLabelService.remove(roomLabelQueryWrapper);
            //删除支付方式列表
            LambdaQueryWrapper<RoomPaymentType> roomPaymentTypeWrapper = new LambdaQueryWrapper<>();
            roomPaymentTypeWrapper.eq(RoomPaymentType::getRoomId,roomSubmitVo.getId());
            paymentTypeService.remove(roomPaymentTypeWrapper);
            //删除可选租期列表
            LambdaQueryWrapper<RoomLeaseTerm> roomLeaseTermWrapper = new LambdaQueryWrapper<>();
            roomLeaseTermWrapper.eq(RoomLeaseTerm::getRoomId,roomSubmitVo.getId());
            roomLeaseTermService.remove(roomLeaseTermWrapper);
        }

        //保存照片信息
        List<GraphVo> graphVoList = roomSubmitVo.getGraphVoList();
        if (!CollectionUtils.isEmpty(graphVoList)) {
            ArrayList<GraphInfo> graphInfoList = new ArrayList<>();
            for (GraphVo graphVo : graphVoList) {
                GraphInfo graphInfo = new GraphInfo();
                graphInfo.setItemType(ItemType.ROOM);
                graphInfo.setItemId(roomSubmitVo.getId());
                graphInfo.setName(graphVo.getName());
                graphInfo.setUrl(graphVo.getUrl());
                graphInfoList.add(graphInfo);
            }
            graphInfoService.saveBatch(graphInfoList);
        }
        //保存属性值信息
        List<Long> attrValueIds = roomSubmitVo.getAttrValueIds();
        if (!CollectionUtils.isEmpty(attrValueIds)) {
            List<RoomAttrValue> roomAttrValueList = new ArrayList<>();
            for (Long attrValueId : attrValueIds) {
                RoomAttrValue roomAttrValue = RoomAttrValue.builder().roomId(roomSubmitVo.getId()).attrValueId(attrValueId).build();
                roomAttrValueList.add(roomAttrValue);
            }
            roomAttrValueService.saveBatch(roomAttrValueList);
        }
        //保存配套信息
        List<Long> facilityInfoIds = roomSubmitVo.getFacilityInfoIds();
        if (!CollectionUtils.isEmpty(facilityInfoIds)) {
            List<RoomFacility> roomFacilityList = new ArrayList<>();
            for (Long facilityInfoId : facilityInfoIds) {
                RoomFacility roomFacility = RoomFacility.builder().roomId(roomSubmitVo.getId()).facilityId(facilityInfoId).build();
                roomFacilityList.add(roomFacility);
            }
            roomFacilityService.saveBatch(roomFacilityList);
        }
        //保存标签信息
        List<Long> labelInfoIds = roomSubmitVo.getLabelInfoIds();
        if (!CollectionUtils.isEmpty(labelInfoIds)) {
            ArrayList<RoomLabel> roomLabelList = new ArrayList<>();
            for (Long labelInfoId : labelInfoIds) {
                RoomLabel roomLabel = RoomLabel.builder().roomId(roomSubmitVo.getId()).labelId(labelInfoId).build();
                roomLabelList.add(roomLabel);
            }
            roomLabelService.saveBatch(roomLabelList);
        }
        //保存支付方式
        List<Long> paymentTypeIds = roomSubmitVo.getPaymentTypeIds();
        if(!CollectionUtils.isEmpty(paymentTypeIds)){
            ArrayList<RoomPaymentType> roomPaymentTypes = new ArrayList<>();
            for (Long roomPaymentTypeId : paymentTypeIds) {
                RoomPaymentType roomPaymentType = RoomPaymentType.builder().roomId(roomSubmitVo.getId()).paymentTypeId(roomPaymentTypeId).build();
                roomPaymentTypes.add(roomPaymentType);
            }
        }
        //保存租期
        List<Long> leaseTermIds = roomSubmitVo.getLeaseTermIds();
        if (!CollectionUtils.isEmpty(leaseTermIds)) {
            ArrayList<RoomLeaseTerm> roomLeaseTerms = new ArrayList<>();
            for (Long leaseTermId : leaseTermIds) {
                RoomLeaseTerm roomLeaseTerm = RoomLeaseTerm.builder().roomId(roomSubmitVo.getId()).leaseTermId(leaseTermId).build();
                roomLeaseTerms.add(roomLeaseTerm);
            }
            roomLeaseTermService.saveBatch(roomLeaseTerms);
        }

    }

    @Override
    public IPage<RoomItemVo> pageItem(IPage<RoomItemVo> page, RoomQueryVo queryVo) {

        return roomInfoMapper.pageItem(page,queryVo);
    }

    @Override
    public RoomDetailVo getDetailById(Long id) {
        //查询公寓信息
        RoomInfo roomInfo = roomInfoMapper.selectById(id);
        //查询所属公寓信息
        ApartmentInfo apartmentInfo = apartmentInfoMapper.selectById(roomInfo.getApartmentId());
        //查询图片列表
        List<GraphVo> graphVo = graphInfoMapper.selectListByItemTypeAndId(ItemType.ROOM,id);
        //查询属性列表
        List<AttrValueVo> attrValueVoList = attrValueMapper.selectListByRoomId(ItemType.ROOM,id);
        //查询配套基础信息
        List<FacilityInfo> facilityInfoList = facilityInfoMapper.selectListByRoomId(id);
        //查询标签属性列表
        List<LabelInfo> labelInfoList = labelInfoMapper.selectListByRoomId(ItemType.ROOM,id);
        //查询支付方式列表
        List<PaymentType> paymentTypeList = paymentTypeMapper.selectListByRoomId(id);
        //查询可选租期列表
        List<LeaseTerm> leaseTermList = leaseTermMapper.selectListByRoomId(id);

        RoomDetailVo roomDetailVo = new RoomDetailVo();
        BeanUtils.copyProperties(roomInfo,roomDetailVo);
        roomDetailVo.setApartmentInfo(apartmentInfo);
        roomDetailVo.setGraphVoList(graphVo);
        roomDetailVo.setAttrValueVoList(attrValueVoList);
        roomDetailVo.setFacilityInfoList(facilityInfoList);
        roomDetailVo.setLabelInfoList(labelInfoList);
        roomDetailVo.setPaymentTypeList(paymentTypeList);
        roomDetailVo.setLeaseTermList(leaseTermList);

        return roomDetailVo;
    }

    @Override
    public void removeRoomById(Long id) {
        super.removeById(id);
        //删除图片信息
        LambdaQueryWrapper<GraphInfo> graphQueryWrapper = new LambdaQueryWrapper<>();
        graphQueryWrapper.eq(GraphInfo::getItemType, ItemType.ROOM);
        graphQueryWrapper.eq(GraphInfo::getItemId, id);
        graphInfoService.remove(graphQueryWrapper);
        //删除属性信息列表
        LambdaQueryWrapper<RoomAttrValue> roomQueryWrapper = new LambdaQueryWrapper<>();
        roomQueryWrapper.eq(RoomAttrValue::getRoomId,id);
        roomAttrValueService.remove(roomQueryWrapper);
        //删除配套信息列表
        LambdaQueryWrapper<RoomFacility> roomFacilityQueryWrapper = new LambdaQueryWrapper<>();
        roomFacilityQueryWrapper.eq(RoomFacility::getRoomId,id);
        roomFacilityService.remove(roomFacilityQueryWrapper);
        //删除标签列表
        LambdaQueryWrapper<RoomLabel> roomLabelQueryWrapper = new LambdaQueryWrapper<>();
        roomLabelQueryWrapper.eq(RoomLabel::getRoomId,id);
        roomLabelService.remove(roomLabelQueryWrapper);
        //删除支付方式列表
        LambdaQueryWrapper<RoomPaymentType> roomPaymentTypeWrapper = new LambdaQueryWrapper<>();
        roomPaymentTypeWrapper.eq(RoomPaymentType::getRoomId,id);
        paymentTypeService.remove(roomPaymentTypeWrapper);
        //删除可选租期列表
        LambdaQueryWrapper<RoomLeaseTerm> roomLeaseTermWrapper = new LambdaQueryWrapper<>();
        roomLeaseTermWrapper.eq(RoomLeaseTerm::getRoomId,id);
        roomLeaseTermService.remove(roomLeaseTermWrapper);
    }
}




