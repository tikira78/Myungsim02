package com.msba.myungsim02.risk;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.msba.myungsim02.R;

public class RiskResultActivity extends AppCompatActivity {

    TextView tv1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_risk_result);

        //
        tv1 = findViewById(R.id.tv_riskresult);

        //intest 가죠오기
        String getString = getIntent().getStringExtra("check");
        Log.i("crkim",getString);

        //문자열 검색
        if(getString.contains("1")){ Log.i("crkim","message: 흡연"); tv1.append("○ 담배 속의 일산화탄소, 니코틴 등의 유해물질이 심장혈관에 악영향을 미치고 혈관 수축을 일으킵니다.\n\n"); }
        if(getString.contains("2")){ Log.i("crkim","message: 음주"); tv1.append("○ 술은 고칼로리 식품으로 알코올은 혈액중 중성지방과 콜레스테롤을 증가시킵니다.\n\n"); }
        if(getString.contains("3")){ Log.i("crkim","message: 고혈압"); tv1.append("○ 혈압의 상승은 동맥경화 발생요인을 증가시키는 혈관벽에 스트레스를 줍니다." +
                "\n 고혈압을 개선하려면 체중조절, 건강한 식품 섭취, 적절한 운동, 절주, 필요시 약물치료를 해야하며, 나의 혈압상태에 늘 관심을 가지고 가능하면 매일 가정에서 혈압을 측정합니다.\n\n"); }
        if(getString.contains("4")){ Log.i("crkim","message: 당뇨"); tv1.append("○ 당이 높아지면 혈관벽의 손상이 빨라지면서 동맥경화증으로 발전합니다.\n\n"); }
        if(getString.contains("5")){ Log.i("crkim","message: 스트레스"); tv1.append("○ 스트레스는 교감신경을 흥분시켜 심장을 빨리 뛰게 하고 혈관을 수축시켜 혈압을 상승시킵니다.\n\n"); }
        if(getString.contains("6")){ Log.i("crkim","message: 콜레스테롤"); tv1.append("○ 혈중에 콜레스테롤이 높으면 혈관벽에 쌓여 혈관을 좁게 만드는 원인이 됩니다. 식습관의 개선, 비만의 예방, " +
                "적절한 육체적인 운동으로 고콜레스테롤 혈증을 개선할 수 있습니다.\n 다양한 과일이나 야채, 다양한 곡물(빵, 씨리얼, 쌀, 파스타), 저지방 또는 무지방 우유, 기름기 쫙 뺀 육류, 등푸른 생선, 콩, 호두, 완두콩 등을 섭취하세요." +
                "\n 하지만 고지방우유, 아이스크림, 버터, 계란노른자, 치즈, 소세지, 핫도그, 오리, 거위고기, 포화자빙이 다량 함유된 각종 오일 및 튀김 음식은 삼가하세요.\n\n"); }
        if(getString.contains("7")){ Log.i("crkim","message: 비만"); tv1.append("○ 과체중은 심장에 부담을 늘리고 고혈압, 고지혈증, 당뇨 등을 유발합니다.\n\n"); }
        if(getString.contains("8")||getString.contains("9")){ Log.i("crkim","message: 가족력"); tv1.append("○ 가족력이 있는 경우에는 위험도가 높아지기 때문에 심혈관질환의 위험에 주의를 기울입니다.\n\n"); }
        if(getString.contains("a")){ Log.i("crkim","message: 나이"); tv1.append("○ 나이가 많아질 수록 위험도는 증가합니다.\n\n"); }
        if(getString.contains("b")){ Log.i("crkim","message: 성별"); tv1.append("○ 일반적으로 남성의 심혈관질환 발생위험이 높지만, 여자도 폐경 후에는 위험도가 남자만큼 높아집니다.\n\n"); }



    }
}