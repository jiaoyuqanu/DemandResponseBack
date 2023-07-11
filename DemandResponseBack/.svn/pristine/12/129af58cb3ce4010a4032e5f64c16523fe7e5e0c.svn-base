package com.xqxy.dr.modular.data.result;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class WorkProjectInfoResult {

    private Integer custIntegratorSize;
    private Integer consSize;
    private Integer integerConsSize;
    private ContractCap contractCap;
    private ContractBackupCap contractBackupCap;
    private EventStats eventStats;
    private SubsidyInfo subsidyInfo;

    /**
     * 签约负荷
     */
    @Data
    public static class ContractCap{
        private BigDecimal desMaxPower;
        private BigDecimal desHourMaxPower;
        private BigDecimal desMinusMaxPower;
        private BigDecimal desSecondMaxPower;
        private BigDecimal risReserveMaxPower;
    }

    /**
     * 签约备用容量
     */
    @Data
    public static class ContractBackupCap{
        private BigDecimal reserveDesMaxPower;
        private BigDecimal realTimeDesMaxPower;
        private BigDecimal reserveRisPower;
    }

    /**
     * 需求响应事件
     */
    @Data
    public static class EventStats{
        private Integer initSize;
        private Integer preRunSize;
        private Integer runningSize;
        private Integer finishedSize;
        private Integer terminationSize;
    }

    /**
     * 响应补偿
     */
    @Data
    public static class SubsidyInfo{
        private BigDecimal totalSubsidyAmount;

        /**
         * 是集成商
         */
        private BigDecimal integratorSubsidyAmount;

        /**
         * 独立参与用户
         */
        private BigDecimal notIntegratorSubsidyAmount;

        /**
         * 代理参与用户  补偿金额
         */
        private BigDecimal consSubsidyAmount;
    }
}
