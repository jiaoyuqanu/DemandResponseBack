package com.xqxy.dr.modular.prediction.param;

import com.xqxy.dr.modular.event.entity.ConsBaseline;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ConsCurveBase extends ConsBaseline {

    public ConsCurveBase() {
        super();
    }

    //区域ID
    private String areaId;
    //区域ID集
    private List<String> areaIds;

    private  List<Long> electricalAreaIds;


    public BigDecimal getPByNum(int num){
        switch (num){
            case 1:  return  getP1();
            case 2:  return  getP2();
            case 3:  return  getP3();
            case 4:  return  getP4();
            case 5:  return  getP5();
            case 6:  return  getP6();
            case 7:  return  getP7();
            case 8:  return  getP8();
            case 9:  return  getP9();
            case 10:  return  getP10();
            case 11:  return  getP11();
            case 12:  return  getP12();
            case 13:  return  getP13();
            case 14:  return  getP14();
            case 15:  return  getP15();
            case 16:  return  getP16();
            case 17:  return  getP17();
            case 18:  return  getP18();
            case 19:  return  getP19();
            case 20:  return  getP20();
            case 21:  return  getP21();
            case 22:  return  getP22();
            case 23:  return  getP23();
            case 24:  return  getP24();
            case 25:  return  getP25();
            case 26:  return  getP26();
            case 27:  return  getP27();
            case 28:  return  getP28();
            case 29:  return  getP29();
            case 30:  return  getP30();
            case 31:  return  getP31();
            case 32:  return  getP32();
            case 33:  return  getP33();
            case 34:  return  getP34();
            case 35:  return  getP35();
            case 36:  return  getP36();
            case 37:  return  getP37();
            case 38:  return  getP38();
            case 39:  return  getP39();
            case 40:  return  getP40();
            case 41:  return  getP41();
            case 42:  return  getP42();
            case 43:  return  getP43();
            case 44:  return  getP44();
            case 45:  return  getP45();
            case 46:  return  getP46();
            case 47:  return  getP47();
            case 48:  return  getP48();
            case 49:  return  getP49();
            case 50:  return  getP50();
            case 51:  return  getP51();
            case 52:  return  getP52();
            case 53:  return  getP53();
            case 54:  return  getP54();
            case 55:  return  getP55();
            case 56:  return  getP56();
            case 57:  return  getP57();
            case 58:  return  getP58();
            case 59:  return  getP59();
            case 60:  return  getP60();
            case 61:  return  getP61();
            case 62:  return  getP62();
            case 63:  return  getP63();
            case 64:  return  getP64();
            case 65:  return  getP65();
            case 66:  return  getP66();
            case 67:  return  getP67();
            case 68:  return  getP68();
            case 69:  return  getP69();
            case 70:  return  getP70();
            case 71:  return  getP71();
            case 72:  return  getP72();
            case 73:  return  getP73();
            case 74:  return  getP74();
            case 75:  return  getP75();
            case 76:  return  getP76();
            case 77:  return  getP77();
            case 78:  return  getP78();
            case 79:  return  getP79();
            case 80:  return  getP80();
            case 81:  return  getP81();
            case 82:  return  getP82();
            case 83:  return  getP83();
            case 84:  return  getP84();
            case 85:  return  getP85();
            case 86:  return  getP86();
            case 87:  return  getP87();
            case 88:  return  getP88();
            case 89:  return  getP89();
            case 90:  return  getP90();
            case 91:  return  getP91();
            case 92:  return  getP92();
            case 93:  return  getP93();
            case 94:  return  getP94();
            case 95:  return  getP95();
            case 96:  return  getP96();
            default:break;
        }
        return null;
    }
    public  void setPointByNum(int num,BigDecimal data){
        switch (num){
            case 1:setP1(data); break;
            case 2:setP2(data); break;
            case 3:setP3(data); break;
            case 4:setP4(data); break;
            case 5:setP5(data); break;
            case 6:setP6(data); break;
            case 7:setP7(data); break;
            case 8:setP8(data); break;
            case 9:setP9(data); break;
            case 10:setP10(data); break;
            case 11:setP11(data); break;
            case 12:setP12(data); break;
            case 13:setP13(data); break;
            case 14:setP14(data); break;
            case 15:setP15(data); break;
            case 16:setP16(data); break;
            case 17:setP17(data); break;
            case 18:setP18(data); break;
            case 19:setP19(data); break;
            case 20:setP20(data); break;
            case 21:setP21(data); break;
            case 22:setP22(data); break;
            case 23:setP23(data); break;
            case 24:setP24(data); break;
            case 25:setP25(data); break;
            case 26:setP26(data); break;
            case 27:setP27(data); break;
            case 28:setP28(data); break;
            case 29:setP29(data); break;
            case 30:setP30(data); break;
            case 31:setP31(data); break;
            case 32:setP32(data); break;
            case 33:setP33(data); break;
            case 34:setP34(data); break;
            case 35:setP35(data); break;
            case 36:setP36(data); break;
            case 37:setP37(data); break;
            case 38:setP38(data); break;
            case 39:setP39(data); break;
            case 40:setP40(data); break;
            case 41:setP41(data); break;
            case 42:setP42(data); break;
            case 43:setP43(data); break;
            case 44:setP44(data); break;
            case 45:setP45(data); break;
            case 46:setP46(data); break;
            case 47:setP47(data); break;
            case 48:setP48(data); break;
            case 49:setP49(data); break;
            case 50:setP50(data); break;
            case 51:setP51(data); break;
            case 52:setP52(data); break;
            case 53:setP53(data); break;
            case 54:setP54(data); break;
            case 55:setP55(data); break;
            case 56:setP56(data); break;
            case 57:setP57(data); break;
            case 58:setP58(data); break;
            case 59:setP59(data); break;
            case 60:setP60(data); break;
            case 61:setP61(data); break;
            case 62:setP62(data); break;
            case 63:setP63(data); break;
            case 64:setP64(data); break;
            case 65:setP65(data); break;
            case 66:setP66(data); break;
            case 67:setP67(data); break;
            case 68:setP68(data); break;
            case 69:setP69(data); break;
            case 70:setP70(data); break;
            case 71:setP71(data); break;
            case 72:setP72(data); break;
            case 73:setP73(data); break;
            case 74:setP74(data); break;
            case 75:setP75(data); break;
            case 76:setP76(data); break;
            case 77:setP77(data); break;
            case 78:setP78(data); break;
            case 79:setP79(data); break;
            case 80:setP80(data); break;
            case 81:setP81(data); break;
            case 82:setP82(data); break;
            case 83:setP83(data); break;
            case 84:setP84(data); break;
            case 85:setP85(data); break;
            case 86:setP86(data); break;
            case 87:setP87(data); break;
            case 88:setP88(data); break;
            case 89:setP89(data); break;
            case 90:setP90(data); break;
            case 91:setP91(data); break;
            case 92:setP92(data); break;
            case 93:setP93(data); break;
            case 94:setP94(data); break;
            case 95:setP95(data); break;
            case 96:setP96(data); break;
            default:break;
        }
    }

}