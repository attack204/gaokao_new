package com.gaokao.common.service;

import com.alibaba.fastjson.JSON;
import com.gaokao.common.dao.*;
import com.gaokao.common.meta.po.*;
import com.gaokao.common.meta.vo.advise.AutoGenerateFormParams;
import com.gaokao.common.meta.vo.advise.FilterParams;
import com.gaokao.common.meta.vo.advise.AdviseVO;
import com.gaokao.common.meta.vo.volunteer.UserFormDetailVO;
import com.gaokao.common.meta.vo.volunteer.VolunteerVO;
import com.gaokao.common.utils.UserUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author MaeYon-Z
 *  date 2021-08-09
 */
@Slf4j
@Service
public class AdviseServiceImpl implements AdviseService{

    @Autowired
    private UserMemberDao userMemberDao;

    @Autowired
    private ScoreRankDao scoreRankDao;

    @Autowired
    private AdviseDao adviseDao;

    @Autowired
    private UserFormDao userFormDao;

    @Autowired
    private FormVolunteerDao formVolunteerDao;

    @Autowired
    private FilterDataDao filterDataDao;

    @Autowired
    private UserStarDao userStarDao;

    private static List<VolunteerVO> volunteerVOList;

    private static Map<String, List<FilterData>> filterDataS = new HashMap<>();

    @Override
    public Integer getUserRank(Integer score){
        Integer totalNums = scoreRankDao.findTotalNumsByScore(score);
        if(totalNums == null){
            if(score > 400){
                totalNums = 50;
            }
            else {
                totalNums = 548762;
            }
        }
        return totalNums;
    }

    private Integer getRate(Integer rank, Integer guess){
        Integer dif = Math.abs(rank - guess);
        Integer rate = 0;
        if(rank > guess){//用户排名大于预估最低录取名次
            if(dif < 1000)
                rate = 60 - dif/100;
            else
                rate = 50 - (dif-1000)/50;
            if (rate < 10)
                rate = 10;
        }
        else if(rank <= guess){//用户排名小于预估最低录取名次
            if(dif < 1000)
                rate = 60 + dif/100;
            else
                rate = 70 + (dif-1000)/50;
            if(rate > 95)
                rate = 95;
        }
        return rate;
    }

    private Map<String, Boolean> start(FilterParams filterParams){
        if(volunteerVOList == null){
            List<Volunteer> volunteerList = adviseDao.getAllVolunteer();
            List<VolunteerVO> volunteerVOS = new ArrayList<>();
            volunteerList.forEach(volunteer -> {
                VolunteerVO volunteerVO = new VolunteerVO();
                BeanUtils.copyProperties(volunteer, volunteerVO);
                if(volunteer.getVolunteerSection() == 1){
                    volunteerVO.setVolunteerSection(true);
                }
                else{
                    volunteerVO.setVolunteerSection(false);
                }
                volunteerVO.setSubjectRestrictionDetail(JSON.parseArray(volunteer.getSubjectRestrictionDetail(), Integer.class));
                volunteerVO.setMyStar(false);
                volunteerVOS.add(volunteerVO);
            });
            volunteerVOList = volunteerVOS;
        }

        if(filterDataS.isEmpty()) {
            for (int i = 1; i <= 4; i++) {
                List<FilterData> secondList = filterDataDao.findSonsByFatherId(i);
                if (i == 1) {
                    filterDataS.put("批次", secondList);
                    continue;
                }
                if(i == 2){
                    List<FilterData> thirdList = new ArrayList<>();
                    for (int j = 0; j < secondList.size(); j++) {
                        thirdList.addAll(filterDataDao.findSonsByFatherId(secondList.get(j).getId()));
                    }
                    filterDataS.put("地区", thirdList);
                    continue;
                }
                if(i == 3){
                    for(int j = 0; j < secondList.size(); j++){
                        String label = secondList.get(j).getLabel();
                        if(label.equals("大学特色")){
                            List<FilterData> teSeList = filterDataDao.findSonsByFatherId(secondList.get(j).getId());
                            filterDataS.put(label, teSeList);
                        }
                        if(label.equals("办学性质")){
                            List<FilterData> xingZhiList = filterDataDao.findSonsByFatherId(secondList.get(j).getId());
                            filterDataS.put(label, xingZhiList);
                        }
                        if(label.equals("大学类型")){
                            List<FilterData> typeList = filterDataDao.findSonsByFatherId(secondList.get(j).getId());
                            filterDataS.put(label, typeList);
                        }
                    }
                }
            }
        }

        //接下来构造存储过滤条件的map
        Map<String, Boolean> conditionsMap = new HashMap<>();
        if(filterParams.getBatch().size() == 0){
            conditionsMap.put("1",true);
            conditionsMap.put("2",true);
        }else {
            for(int i = 0; i < filterParams.getBatch().size(); i++){
                FilterData filterData = filterDataDao.getOneById(filterParams.getBatch().get(i));
                String label = filterData.getLabel();
                if(label.equals("普通一段")){
                    conditionsMap.put("1", true);
                }else{
                    conditionsMap.put("2", true);
                }
            }
        }

        if(filterParams.getRegion().size() == 0){
            /*
            conditionsMap.put("北京",true);
            conditionsMap.put("南京",true);
            conditionsMap.put("上海",true);
            conditionsMap.put("天津",true);
            conditionsMap.put("武汉",true);
            conditionsMap.put("济南",true);
            */
            List<FilterData> regionList = filterDataS.get("地区");
            for(int i = 0; i < regionList.size(); i++){
                String label = regionList.get(i).getLabel();
                Integer j = label.indexOf("市");
                if(j != -1) {
                    label = label.substring(0, j);
                    conditionsMap.put(label, true);
                }
                else{
                    conditionsMap.put(label,true);
                }
            }

        }else{
            for(int i = 0; i < filterParams.getRegion().size(); i++){
                FilterData filterData = filterDataDao.getOneById(filterParams.getRegion().get(i));
                String label = filterData.getLabel();
                Integer j = label.indexOf("市");
                if(j != -1) {
                    label = label.substring(0, j);
                    conditionsMap.put(label, true);
                }
                else{
                    conditionsMap.put(label,true);
                }
            }
        }

        if(filterParams.getSchoolTeSe().size() == 0){
            /*
            conditionsMap.put("本科", true);
            */
            List<FilterData> teSeList = filterDataS.get("大学特色");
            for(int i = 0; i < teSeList.size(); i++){
                String label = teSeList.get(i).getLabel();
                conditionsMap.put(label, true);
            }

        }else{
            for(int i = 0; i < filterParams.getSchoolTeSe().size(); i++){
                FilterData filterData = filterDataDao.getOneById(filterParams.getSchoolTeSe().get(i));
                String label = filterData.getLabel();
                conditionsMap.put(label, true);
            }
        }

        if(filterParams.getSchoolXingZhi().size() == 0){
            /*
            conditionsMap.put("公办", true);
            */
            List<FilterData> xingZhiList = filterDataS.get("办学性质");
            for(int i = 0; i < xingZhiList.size(); i++){
                String label = xingZhiList.get(i).getLabel();
                conditionsMap.put(label, true);
            }

        }else{
            for(int i = 0; i < filterParams.getSchoolXingZhi().size(); i++){
                FilterData filterData = filterDataDao.getOneById(filterParams.getSchoolXingZhi().get(i));
                String label = filterData.getLabel();
                conditionsMap.put(label, true);
            }
        }

        if(filterParams.getSchoolType().size() == 0){
            /*
            conditionsMap.put("综合", true);
            */
            List<FilterData> typeList = filterDataS.get("大学类型");
            for(int i = 0; i < typeList.size(); i++){
                String label = typeList.get(i).getLabel();
                conditionsMap.put(label, true);
            }

        }else{
            for(int i = 0; i < filterParams.getSchoolType().size(); i++){
                FilterData filterData = filterDataDao.getOneById(filterParams.getSchoolType().get(i));
                String label = filterData.getLabel();
                conditionsMap.put(label, true);
            }
        }
        return conditionsMap;
    }

    private Map<Long, Boolean> getSchoolAndMajorMap(FilterParams filterParams){

        if(filterParams.getMajorName() == null){
            filterParams.setMajorName("");
        }
        if(filterParams.getUniversityName() == null){
            filterParams.setUniversityName("");
        }

        if(filterParams.getMajorName().length() != 0 || filterParams.getUniversityName().length() != 0) {
            Map<Long, Boolean> map = new HashMap<>();
            String universityName = filterParams.getUniversityName();
            String majorName = filterParams.getMajorName();

            String schoolParm = "%";
            String majorParm = "%";
            for (int i = 0; i < majorName.length(); i++) {
                majorParm += majorName.substring(i, i + 1) + "%";
            }
            for (int i = 0; i < universityName.length(); i++) {
                schoolParm += universityName.substring(i, i + 1) + "%";
            }
            List<Volunteer> volunteerList = adviseDao.getVolunteerByMajorAndSchool(schoolParm, majorParm);
            volunteerList.forEach(volunteer -> {
                map.put(volunteer.getId(), true);
            });
            return map;
        }
        return null;
    }

    private Map<Long, Boolean> getUserStarMap(Long userId){
        Map<Long, Boolean> userStarMap = new HashMap<>();
        UserStar userStars = userStarDao.getUserStars(userId);
        if(userStars == null){
            return null;
        }
        List<Long> userStarList = JSON.parseArray(userStars.getStars(),Long.class);
        userStarList.forEach(userStarId ->{
            userStarMap.put(userStarId, true);
        });

        return userStarMap;
    }

    private Integer findCommon(List<Integer> m, List<Integer> n){
        Integer res = 0;
        for(int i = 0; i < m.size(); i++){
            for (int j = 0; j < n.size(); j++){
                if(m.get(i) == n.get(j)){
                    res ++;
                }
            }
        }
        return res;
    }

    private boolean filter(FilterParams filterParams, Map<String, Boolean> conditionsMap, Map<Long, Boolean> schoolAndmajorMap,List<Integer> subject, VolunteerVO volunteerVO){

        if(schoolAndmajorMap != null){
            if(!schoolAndmajorMap.containsKey(volunteerVO.getId())){
                return false;
            }
        }

        //首先判断选课是否符合
        List<Integer> voluntSubject = volunteerVO.getSubjectRestrictionDetail();
        Integer commom = findCommon(voluntSubject, subject);
        switch(volunteerVO.getSubjectRestrictionType()){
            case 0:
                break;
            case 1:
                if(commom == 1){
                    break;
                }
                else {
                    return false;
                }
            case 2:
                if(commom == 2){
                    break;
                }
                else {
                    return false;
                }
            case 3:
                if(commom == 3){
                    break;
                }
                else {
                    return false;
                }
            case 4:
                if(commom >= 1){
                    break;
                }
                else{
                    return false;
                }
            case 5:
                if(commom >= 1){
                    break;
                }
                else {
                    return false;
                }
            case 6:
                if(commom >= 2){
                    break;
                }
                else {
                    return false;
                }
        }

        /*
           接下来判断录取批次、地区、大学特色、办学性质、大学类型等是否符合条件
                必须同时满足才可以返回true，只要一个不满足就返回false。
        */

        //判断录取批次是否符合
        Integer section;
        if(volunteerVO.getVolunteerSection()){
            section = 1;
        }
        else {
            section = 2;
        }
        if(!conditionsMap.containsKey(section + "")){
            return false;
        }

        //判断地区是否符合
        String city = volunteerVO.getCity();
        if(!conditionsMap.containsKey(city)){
            return false;
        }

        //判断大学特色（985、211、本科、专科）是否符合
        if(conditionsMap.containsKey("985") && conditionsMap.containsKey("211") && conditionsMap.containsKey("本科") && conditionsMap.containsKey("专科")){

        }else{
            if(conditionsMap.containsKey("985") && !volunteerVO.getIs985()){
                return false;
            }
            if(conditionsMap.containsKey("211") && !volunteerVO.getIs211()){

                return false;
            }
            if(conditionsMap.containsKey("本科") && !volunteerVO.getUndergraduateSchoolIsOrNot()){
                return false;
            }
            if(conditionsMap.containsKey("专科") && !volunteerVO.getJuniorSchoolIsOrNot()){
                return false;
            }
        }

        //判断办学性质（公办、民办）是否符合
        if(conditionsMap.containsKey("公办") && conditionsMap.containsKey("民办")){

        }else{
            if(conditionsMap.containsKey("公办") && volunteerVO.getPrivateIsOrNot()){
                return false;
            }
            if(conditionsMap.containsKey("民办") && volunteerVO.getPublicIsOrNot()){
                return false;
            }
        }

        //判读大学类型是否符合
        String category = volunteerVO.getCategory();
        if(!conditionsMap.containsKey(category)){
            return false;
        }

        return true;
    }

    private Boolean ifStar(Map<Long, Boolean> starMap, VolunteerVO volunteerVO){
        if(starMap == null){
            return false;
        }
        return starMap.containsKey(volunteerVO.getId());
    }

    private List<AdviseVO> listAll(FilterParams filterParams, Integer score, List<Integer> subject){
        List<AdviseVO> adviseVOList = new ArrayList<>();

        Map<String, Boolean> conditionsMap = start(filterParams);

        Map<Long, Boolean> schoolAndMajorMap = getSchoolAndMajorMap(filterParams);

        Map<Long, Boolean> starMap = getUserStarMap(UserUtils.getUserId());

        Integer rank = getUserRank(score);

        List<VolunteerVO> volunteerVOList1 = volunteerVOList;

        volunteerVOList1.forEach(volunteerVO -> {
            if(filter(filterParams, conditionsMap, schoolAndMajorMap, subject, volunteerVO)){
                if(ifStar(starMap, volunteerVO)){
                    volunteerVO.setMyStar(true);
                }else {
                    volunteerVO.setMyStar(false);
                }
                Integer rate = getRate(rank, volunteerVO.getPosition());
                String rateDesc = "";
                if(rate <= 50)
                    rateDesc = "难录取";
                else if(rate > 50 && rate <= 60)
                    rateDesc = "可冲击";
                else if(rate > 60 && rate <= 80)
                    rateDesc = "较稳妥";
                else if(rate > 80 && rate < 95)
                    rateDesc = "可保底";
                else if(rate == 95)
                    rateDesc = "浪费分";
                AdviseVO adviseVO = new AdviseVO();
                adviseVO.setRate(rate);
                adviseVO.setRateDesc(rateDesc);
                adviseVO.setVolunteerVO(volunteerVO);
                adviseVOList.add(adviseVO);
            }
        });
        return adviseVOList.stream().sorted(Comparator.comparing(AdviseVO::getRate))
                .collect(Collectors.toList());
    }

    @Override
    public Page<AdviseVO> list(FilterParams filterParams){
        Long userId = UserUtils.getUserId();
        UserMember userMember = userMemberDao.findUserMemberById(userId);
        Integer score = userMember.getScore();
        List<Integer> subject = JSON.parseArray(userMember.getSubject(), Integer.class);
        List<AdviseVO> adviseVOS = listAll(filterParams, score, subject);
        if(filterParams.getType() == 0){
            Pageable pageable = PageRequest.of(filterParams.getPage() - 1, filterParams.getLimit());
            Integer fromIndex = (filterParams.getPage() - 1) * filterParams.getLimit();
            Integer toIndex = fromIndex + filterParams.getLimit();
            if(toIndex >= adviseVOS.size()){
                toIndex = adviseVOS.size();
            }
            List<AdviseVO> adviseVOS1 = adviseVOS.subList(fromIndex, toIndex);
            return new PageImpl<>(adviseVOS1, pageable, adviseVOS.size());
        }
        Map<String, List<AdviseVO>> map = new HashMap<>();
        List<AdviseVO> adviseVOList = new ArrayList<>();
        adviseVOS.stream().collect(Collectors.groupingBy(AdviseVO::getRateDesc,Collectors.toList()))
                .forEach(map::put);
        switch (filterParams.getType()){
            case 1:
                adviseVOList = map.get("可冲击");
                break;
            case 2:
                adviseVOList = map.get("较稳妥");
                break;
            case 3:
                adviseVOList = map.get("可保底");
                break;
            default:
                adviseVOList = adviseVOS;
            }
            if(adviseVOList == null){
                adviseVOList = new ArrayList<>();
            }
            adviseVOList.sort(Comparator.comparing(AdviseVO::getRate).reversed());

        Integer fromIndex = (filterParams.getPage() - 1) * filterParams.getLimit();
        Integer toIndex = fromIndex + filterParams.getLimit();
        if(toIndex >= adviseVOList.size()){
            toIndex = adviseVOList.size();
        }
        List<AdviseVO> adviseVOList1 = adviseVOList.subList(fromIndex, toIndex);
        return new PageImpl<>(adviseVOList1, PageRequest.of(filterParams.getPage() - 1, filterParams.getLimit()), adviseVOList.size());
    }

    @Override
    public UserFormDetailVO generateVoluntForm(AutoGenerateFormParams autoGenerateFormParams){
        Long userId = autoGenerateFormParams.getUserId();
        UserMember userMember = userMemberDao.findUserMemberById(userId);
        //首先向tb_user_form中添加一条数据，表示生成一张表
        Date date = new Date();
        Long timestamp = date.getTime();
        UserForm userForm = new UserForm();
        userForm.setUserId(userId);
        userForm.setName(userMember.getScore() + "-新建志愿表");
        userForm.setScore(userMember.getScore());
        List<Integer> subject1 = JSON.parseArray(userMember.getSubject(), Integer.class);
        String s = JSON.toJSONString(subject1);
        userForm.setSubject(s);
        userForm.setGeneratedType(true);
        userForm.setCurrent(false);
        userForm.setGeneratedTime(timestamp);
        userFormDao.save(userForm);
        UserForm userForm1 = userFormDao.findForm(userId, timestamp);  //获取刚生成的表的信息

        //接下来向tb_form_volunteer表中插入新自动生成的表的志愿
        List<AdviseVO> adviseVOList = listAll(autoGenerateFormParams, userMember.getScore(), subject1);
        Map<String, List<AdviseVO>> map = new HashMap<>();
        adviseVOList.stream().collect(Collectors.groupingBy(AdviseVO::getRateDesc,Collectors.toList()))
                .forEach(map::put);
        List<AdviseVO> chongList = map.get("可冲击");
        List<AdviseVO> wenList = map.get("较稳妥");
        List<AdviseVO> baoList = map.get("可保底");
        List<AdviseVO> hardList = map.get("难录取");
        List<AdviseVO> wasteList = map.get("浪费分");
        if(chongList == null){
            chongList = new ArrayList<>();
        }
        if(wenList == null){
            wenList = new ArrayList<>();
        }
        if(baoList == null){
            baoList = new ArrayList<>();
        }
        if(hardList == null){
            hardList = new ArrayList<>();
        }
        if(wasteList == null){
            wasteList = new ArrayList<>();
        }
        List<VolunteerVO> volunteerVOList = new ArrayList<>();
        //接下来构造96个志愿的列表，共分为四种情况

        /*
        第一种情况，筛选后的冲、稳、保数量均足够。
         */
        if(chongList.size() > autoGenerateFormParams.getChongRate() && baoList.size() > autoGenerateFormParams.getBaoRate()
            && wenList.size() > autoGenerateFormParams.getWenRate()){
            for(int i = 0; i < autoGenerateFormParams.getChongRate(); i++){
                volunteerVOList.add(chongList.get(i).getVolunteerVO());
            }
            for(int i = 0; i < autoGenerateFormParams.getWenRate(); i++){
                volunteerVOList.add(wenList.get(i).getVolunteerVO());
            }
            for(int i = 0; i < autoGenerateFormParams.getBaoRate(); i++){
                volunteerVOList.add(baoList.get(i).getVolunteerVO());
            }
        }
       /* *
        * 第二种情况：筛选后冲、稳、保某一个或两个数量不够，但三者总数够96个。
        * 处理方案为：先按照筛选后的向志愿表中添加，然后用数量多的list去填够96个，优先顺序为保、稳、冲
       */
        else if(chongList.size() + wenList.size() + baoList.size() >= 96){
            int c = 0, w = 0, b = 0;
            boolean isFull = false;
            for(c = 0; c < chongList.size() && c < autoGenerateFormParams.getChongRate(); c++){
                volunteerVOList.add(chongList.get(c).getVolunteerVO());
            }
            for(w = 0; w < wenList.size() && w < autoGenerateFormParams.getWenRate(); w++){
                volunteerVOList.add(wenList.get(w).getVolunteerVO());
            }
            for(b = 0; b < baoList.size() && b < autoGenerateFormParams.getBaoRate(); b++){
                volunteerVOList.add(baoList.get(b).getVolunteerVO());
            }
            if(b < baoList.size()){
                for(b = b; b < baoList.size(); b++){
                    volunteerVOList.add(baoList.get(b).getVolunteerVO());
                    if(volunteerVOList.size() == 96){
                        isFull = true;
                        break;
                    }
                }
            }
            if(!isFull && w < wenList.size()){
                for(w = w; w < wenList.size(); w++){
                    volunteerVOList.add(c + b, wenList.get(w).getVolunteerVO());
                    if(volunteerVOList.size() == 96){
                        isFull = true;
                        break;
                    }
                }
            }
            if(!isFull && c < chongList.size()){
                for(c = c; c < chongList.size(); c++){
                    volunteerVOList.add(c, chongList.get(c).getVolunteerVO());
                    if(volunteerVOList.size() == 96){
                        isFull = true;
                        break;
                    }
                }
            }

        }
        /*
        * 第三种情况：筛选后冲、稳、保总数不够96个但加上浪费分和难录取的数量够96个。
        * 处理方案为：先用筛选后的浪费分的志愿当作保底的去补充，数量还不够则用筛选后的难录取的志愿当作冲击的志愿去补充。
        **/
        else if(chongList.size() + wenList.size() + baoList.size() + hardList.size() + wasteList.size() >= 96){
            boolean isFull = false;
            for(int i = 0; i < chongList.size(); i++){
                volunteerVOList.add(chongList.get(i).getVolunteerVO());
            }
            for(int i = 0; i < wenList.size(); i++){
                volunteerVOList.add(wenList.get(i).getVolunteerVO());
            }
            for(int i = 0; i < baoList.size(); i++){
                volunteerVOList.add(baoList.get(i).getVolunteerVO());
            }
            for(int i = 0; i < wasteList.size(); i++){
                volunteerVOList.add(wasteList.get(i).getVolunteerVO());
                if(volunteerVOList.size() == 96){
                    isFull = true;
                    break;
                }
            }
            if(!isFull){
                for(int i = 0; i < hardList.size(); i++){
                    volunteerVOList.add(0, hardList.get(i).getVolunteerVO());
                    if(volunteerVOList.size() == 96){
                        isFull = true;
                        break;
                    }
                }
            }
        }
        /*
        * 第四种情况：筛选后的 冲、稳、保、浪费分、难录取加起来总数还不够96个。
        * 处理方案为：先将满足筛选条件的加入进去，然后清空筛选条件重新获得志愿的list，补充至96个。
        **/
        else{
            for(int i = 0; i < hardList.size(); i++){
                volunteerVOList.add(hardList.get(i).getVolunteerVO());
            }
            for(int i = 0; i < chongList.size(); i++){
                volunteerVOList.add(chongList.get(i).getVolunteerVO());
            }
            for(int i = 0; i < wenList.size(); i++){
                volunteerVOList.add(wenList.get(i).getVolunteerVO());
            }
            for(int i = 0; i < baoList.size(); i++){
                volunteerVOList.add(baoList.get(i).getVolunteerVO());
            }
            for(int i = 0; i < wasteList.size(); i++){
                volunteerVOList.add(wasteList.get(i).getVolunteerVO());
            }

            List<Integer> integerList = new ArrayList<>();
            FilterParams filterParams = new FilterParams();
            filterParams.setBatch(autoGenerateFormParams.getBatch());
            filterParams.setRegion(integerList);
            filterParams.setSchoolTeSe(integerList);
            filterParams.setSchoolType(integerList);
            filterParams.setSchoolXingZhi(integerList);
            filterParams.setType(0);
            filterParams.setLimit(autoGenerateFormParams.getLimit());
            filterParams.setPage(autoGenerateFormParams.getPage());
            filterParams.setTotal(autoGenerateFormParams.getTotal());

            List<AdviseVO> adviseVOList1 = list(filterParams).getContent();
            Integer i = 0;
            while(true){
                if(!volunteerVOList.contains(adviseVOList1.get(i).getVolunteerVO())){
                    volunteerVOList.add(adviseVOList1.get(i).getVolunteerVO());
                }
                i ++;
                if(volunteerVOList.size() == 96){
                    break;
                }
            }
        }
        List<FormVolunteer> formVolunteerList = new ArrayList<>();
        for(int i = 0; i < 96; i++){
            FormVolunteer formVolunteer = new FormVolunteer();
            formVolunteer.setFormId(userForm1.getId());
            formVolunteer.setVolunteerSection(volunteerVOList.get(i).getVolunteerSection());
            formVolunteer.setVolunteerPosition(i + 1);
            formVolunteer.setVolunteerId(volunteerVOList.get(i).getId());
            formVolunteerList.add(formVolunteer);
            volunteerVOList.get(i).setVolunteerPosition(i + 1);
        }
        formVolunteerDao.saveAll(formVolunteerList);

        //接下来构造返回对象
        UserFormDetailVO userFormDetailVO = new UserFormDetailVO();
        BeanUtils.copyProperties(userForm, userFormDetailVO);
        userFormDetailVO.setVolunteerList(volunteerVOList);
        userFormDetailVO.setScore(userMember.getScore());
        List<Long> subject = new ArrayList<>();

        String s1 = userForm.getSubject();
        subject = JSON.parseArray(s1, Long.class);
        userFormDetailVO.setSubject(subject);
        return userFormDetailVO;
    }

}