package ssangyong.dclass.seoulexploration.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import ssangyong.dclass.seoulexploration.R;
import ssangyong.dclass.seoulexploration.adapter.SearchListAdapter;
import ssangyong.dclass.seoulexploration.data.TourDAO;
import ssangyong.dclass.seoulexploration.data.TourDTO;
import ssangyong.dclass.seoulexploration.temp.Utils;
import ssangyong.dclass.seoulexploration.view.CategoryManager;

import static ssangyong.dclass.seoulexploration.activity.LoadActivity.sysLanguage;

/********************************************************************************
 * * 작성자 : 박보현
 * * 기  능 : 카테고리 검색 및 직접 검색어 입력하여 검색
 * * 수정내용 :
 * * 버  전 :
 * *******************************************************************************/


public class SearchActivity extends AppCompatActivity implements View.OnClickListener {


    Spinner categorySpinner;
    Spinner subCategorySpinner;
    ArrayAdapter<String> categoryAdapter;
    ArrayAdapter<String> subCategoryAdapter;


    Button cateSearchBtn;
    Button keywordSearchBtn;
    SearchView searchView;

    SearchListAdapter searchListAdapter;
    ListView searchListView;
    List<TourDTO> searchedList = new ArrayList<>();

    InputMethodManager imm;
    String selCateName;
    String SELECTED_TYPE = "";

    boolean isPageOpen = false;

    Animation translateLeftAnim;
    Animation translateRightAnim;

    //슬라이딩으로 보여줄 페이지
    LinearLayout slidingMenuPage;

    Button backBtn;


    @Override
    public void onClick(View v) {
        if (v == cateSearchBtn) {
            hideKeyboard();

            String selSubCateName = (String) subCategorySpinner.getSelectedItem();

            searchedList = searchByCategory(selCateName, selSubCateName);

            searchListAdapter = new SearchListAdapter(this, searchedList, SELECTED_TYPE);
            searchListView.setAdapter(searchListAdapter);

            searchListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                    TourDTO tour = searchedList.get(position);

                    Intent intent = new Intent(getApplicationContext(), DetailActivity.class);
                    intent.putExtra("name", tour.getName());
                    startActivity(intent);
                }
            });

        } else if (v == keywordSearchBtn) {
            String query =  searchView.getQuery().toString();
            submitQuery(query);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar ab = getSupportActionBar();
        ab.setTitle("카테고리로 선택");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xFF293133));
        setContentView(R.layout.activity_search);

        //홈버튼 보여주기
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.menuicon);


        //슬라이드메뉴 res 왼쪽 오른쪽 값 애니메이션 객체에 넣어주기
        translateLeftAnim = AnimationUtils.loadAnimation(this, R.anim.menu_left);
        translateRightAnim = AnimationUtils.loadAnimation(this, R.anim.menu_right);

        //슬라이드메뉴페이지
        slidingMenuPage = (LinearLayout) findViewById(R.id.slidingMenuPage);

        backBtn = (Button) findViewById(R.id.backBtn);

        imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);

        Locale systemLocale = getResources().getConfiguration().locale;
        sysLanguage = systemLocale.getLanguage();

        categorySpinner = (Spinner) findViewById(R.id.cate_spinner);
        subCategorySpinner = (Spinner) findViewById(R.id.sub_cate_spinner);

        cateSearchBtn = (Button) findViewById(R.id.cate_search_btn);
        cateSearchBtn.setOnClickListener(this);


        keywordSearchBtn = (Button) findViewById(R.id.keyword_search_btn);
        keywordSearchBtn.setOnClickListener(this);

        searchListView = (ListView) findViewById(R.id.search_list);

        categoryAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, CategoryManager.getCategory(sysLanguage));
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(categoryAdapter);

        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?>  parent, View view, int position, long id) {
                String categoryName = (String) categorySpinner.getSelectedItem();

                selCateName = categoryName;
                makeSubCategory(categoryName);
            }
            public void onNothingSelected(AdapterView<?>  parent) {
            }
        });



        searchView = (SearchView) findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                submitQuery(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private void submitQuery(String query) {
        hideKeyboard();
        searchedList = searchByKeyword(query);
        if (searchedList.size() == 1) {

            TourDTO tour = searchedList.get(0);
            Intent intent = new Intent(getApplicationContext(), DetailActivity.class);

            intent.putExtra("name", tour.getName());
            startActivity(intent);

        } else if (searchedList.size() > 1) {
            searchListAdapter = new SearchListAdapter(getApplicationContext(), searchedList, SELECTED_TYPE);
            searchListView.setAdapter(searchListAdapter);
            searchListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                    TourDTO tour = searchedList.get(position);
                    /*Utils.toast(getApplication(), "Show Detail : " + tour.getName());*/
                    Intent intent = new Intent(getApplicationContext(), DetailActivity.class);
                    intent.putExtra("name", tour.getName());
                    startActivity(intent);
                }
            });
        } else if (searchedList.size() == 0) {
            Utils.toast(getApplicationContext(), "검색 결과가 없습니다");
        }
    }

    private void hideKeyboard() {
        imm.hideSoftInputFromWindow(searchView.getWindowToken(), 0);
    }

    public void makeSubCategory(String categoryName) {
        if (categoryName.equals("구역") || categoryName.equals("area")) {
            subCategoryAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, CategoryManager.getAreaCategory(sysLanguage));
            SELECTED_TYPE = "AREA";
        } else if (categoryName.equals("테마") || categoryName.equals("theme")) {
            SELECTED_TYPE = "THEME";
            subCategoryAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, CategoryManager.getThemeCategory(sysLanguage));
        }

        subCategoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        subCategorySpinner.setAdapter(subCategoryAdapter);

        subCategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?>  parent, View view, int position, long id) {
//                String categoryName = (String) subCategorySpinner.getSelectedItem();

            }
            public void onNothingSelected(AdapterView<?>  parent) {
            }
        });
    }



    public List<TourDTO> getTourList() {
        TourDAO dao = new TourDAO(this,sysLanguage);
        return dao.selectAll();
    }


    public List<TourDTO> searchByCategory(String cate, String subCate) {
        List<TourDTO> result = new ArrayList<>();
        List<TourDTO> tourList = getTourList();

//        Utils.log("# SEARCH BY CATEGORY: "+cate+" > "+subCate+" in "+tourList.size());
        for (TourDTO tour : tourList) {
            if (cate.equals("구역") || cate.equals("area")) {
                if (tour.getArea().replaceAll("\\s","").equalsIgnoreCase(subCate.trim().replaceAll("\\s", ""))) {
                    result.add(tour);
                }
            } else if (cate.equals("테마") || cate.equals("theme")) {
                if (tour.getTheme().equalsIgnoreCase(subCate)) {
                    result.add(tour);
                }
            }
        }
//        Utils.log("# SEARCH RESULT " + result.size() + "개");
        return result;
    }

    public List<TourDTO> searchByKeyword(String query) {
        List<TourDTO> result = new ArrayList<>();
        List<TourDTO> tourList = getTourList();

//        Utils.log("# SEARCH BY KEYWORD: " + query + " in " + tourList.size());
        for (TourDTO tour : tourList) {

            String target = tour.getName().replaceAll("\\s", "");
            query = query.replaceAll("\\s", "");
            if (target.equalsIgnoreCase(query) || target.toLowerCase().contains(query.toLowerCase())) {
                result.add(tour);
            }
        }
//        Utils.log("# SEARCH RESULT " + result.size() + "개");
        return result;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //홈버튼을 선택하면
        if(id == android.R.id.home){

            if(isPageOpen){ //페이지가 열려있으면
                isPageOpen = false;
                slidingMenuPage.startAnimation(translateLeftAnim);
                slidingMenuPage.setVisibility(View.INVISIBLE);
                backBtn.setVisibility(View.INVISIBLE);
            }
            else{ //페이지가 닫혀있으면
                isPageOpen = true;
                slidingMenuPage.startAnimation(translateRightAnim);
                slidingMenuPage.setVisibility(View.VISIBLE);
                backBtn.setVisibility(View.VISIBLE);
            }
        }

        return super.onOptionsItemSelected(item);
    }


    public void onClickTourInfoBtn(View v) {    //관광 안내소 버튼
        if(isPageOpen){ //페이지가 열려있으면
            isPageOpen = false;
            slidingMenuPage.setVisibility(View.INVISIBLE);
        }
        Intent intent = new Intent(getApplicationContext(),TourInfoActivity.class);
        startActivity(intent);
    }

    public void onClickInfoBtn(View v) {    //출처 안내 버튼
        if(isPageOpen){ //페이지가 열려있으면
            isPageOpen = false;
            slidingMenuPage.setVisibility(View.INVISIBLE);
        }
        Intent intent = new Intent(getApplicationContext(),InfoActivity.class);
        startActivity(intent);
    }

    public void onClickBack(View v){   //다른 곳 눌러도 닫히게
        if(isPageOpen){ //페이지가 열려있으면
            isPageOpen = false;
            slidingMenuPage.startAnimation(translateLeftAnim);
            slidingMenuPage.setVisibility(View.INVISIBLE);
            backBtn.setVisibility(View.INVISIBLE);
        }
    }
}
