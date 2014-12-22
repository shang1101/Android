package com.teknosa.fragments.product;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.SQLException;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.LayoutAnimationController;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.teknosa.R;
import com.teknosa.activities.product.ProductListWithAttributesActivity;
import com.teknosa.adapters.BrandListAdapter;
import com.teknosa.adapters.CategoryLevel3Adapter;
import com.teknosa.adapters.LeftAttributeAdapter;
import com.teknosa.adapters.SelectedBrandListAdapter;
import com.teknosa.adapters.SelectedLeftAttributeAdapter;
import com.teknosa.app.AppData;
import com.teknosa.base.FragmentBase;
import com.teknosa.be.Attribute;
import com.teknosa.be.AttributeValue;
import com.teknosa.be.Category;
import com.teknosa.be.CategoryCheckboxStatus;
import com.teknosa.be.Price;
import com.teknosa.be.product.Brand;
import com.teknosa.custom.RangeSeekBar;
import com.teknosa.custom.RangeSeekBar.OnRangeSeekBarChangeListener;
import com.teknosa.custom.VerticalSeekBar;
import com.teknosa.db.dao.Pr_attributesDAO;
import com.teknosa.db.dao.Pr_categoriesDAO;
import com.teknosa.db.dao.ProductsDAO;
import com.teknosa.db.dao.Qry_parametersDAO;
import com.teknosa.db.tables.Qry_parameters;
import com.teknosa.tools.ExceptionManager;
import com.teknosa.util.NumberUtil;
import com.teknosa.util.StringUtil;
import com.teknosa.util.ViewUtil;

public class ProductListWithAttributesActivity_AttributesFragment extends FragmentBase {

	private BrandListAdapter brandListAdapter;
	private LeftAttributeAdapter leftAttributeAdapter;
	private SelectedLeftAttributeAdapter selectedLeftAttributeAdapter;
	private SelectedBrandListAdapter selectedBrandListAdapter;
	private CategoryLevel3Adapter categoryLevel3Adapter;

	private Pr_attributesDAO pr_attributesDAO;
	private ProductsDAO productsDAO;
	private Pr_categoriesDAO pr_categoriesDAO;
	public Qry_parametersDAO qry_parametersDAO;
	public Qry_parameters qry_parameters;

	private View mLoginStatusView;
	private TextView mLoginStatusMessageView;
	private LoadTask mLoadTask = null;
	private ReLoadTask mReLoadTask = null;
	private ReLoadTaskIndependently mReLoadTaskIndependently = null;
	private TextView minPriceTextView;
	private TextView maxPriceTextView;
	private RangeSeekBar<Integer> seekBar;
	private LinearLayout layout;
	private ListView listView;
	private ExpandableListView expandableListView;
	private ExpandableListView selectedAttributesExpandableListView;
	private ExpandableListView categoriesExpandableListView;
	private TextView categoriesTextView;
	private ScrollView scrollView1;
	private LinearLayout selectedAttributesLinearLayout;
	private ExpandableListView selectedBrandsExpandableListView;
	private ImageButton btnSecilenler;
	private Button btnQueriedCategoryCheckboxStatusClear;
	private Button btnSelectedPricesClear;
	private ImageButton btnFiltrele;
	private ImageButton btnSecilenleriTemizle;
	private CheckBox stokluCheckBox;
	public ProgressDialog progressDialog;
	public ProgressDialog progressDialogReLoad;

	private int seekBarStepSize = 50;
	int min = 0;
	int max = 0;

	int seekBarStepSize_1 = 1;
	int seekBarStepSize_5 = 5;
	int seekBarStepSize_10 = 10;
	int seekBarStepSize_20 = 20;
	int seekBarStepSize_50 = 50;
	int seekBarStepSize_100 = 100;

	double seekBarContStepSize_1 = 1.0;
	double seekBarContStepSize_5 = 2.5;
	double seekBarContStepSize_10 = 7.5;
	double seekBarContStepSize_20 = 15;
	double seekBarContStepSize_50 = 25;
	double seekBarContStepSize_100 = 75;
	boolean dispatchMode = false;

	private String p_from = "Menu";
	private ArrayList<Integer> prod_ids = new ArrayList<Integer>();

	private List<Attribute> attributes;
	private ArrayList<Category> categories;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		pr_attributesDAO = new Pr_attributesDAO(getActivity());
		productsDAO = new ProductsDAO(getActivity());
		pr_categoriesDAO = new Pr_categoriesDAO(getActivity());
		qry_parametersDAO = new Qry_parametersDAO(getActivity());
		qry_parameters = new Qry_parameters();

		Bundle extras = getArguments();
		if (extras != null) {

			p_from = extras.getString("p_from");

		} else {
			// ..oops!
		}

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.fragment_left_attributes, container, false);
		Log.d("On create Dispact mode :", "mode:" + dispatchMode);
		// /////////////////////////////////////////

		int mAnimationDuration = getResources().getInteger(android.R.integer.config_shortAnimTime);
		LayoutAnimationController controller = ViewUtil.getListAnimation(mAnimationDuration);

		expandableListView = (ExpandableListView) v.findViewById(R.id.attributesExpandableListView);
		// expandableListView.setLayoutAnimation(controller);

		selectedAttributesExpandableListView = (ExpandableListView) v.findViewById(R.id.selectedAttributesExpandableListView);
		// selectedAttributesExpandableListView.setLayoutAnimation(controller);

		selectedBrandsExpandableListView = (ExpandableListView) v.findViewById(R.id.selectedBrandsExpandableListView);
		// selectedBrandsExpandableListView.setLayoutAnimation(controller);

		selectedAttributesLinearLayout = (LinearLayout) v.findViewById(R.id.selectedAttributesLinearLayout);
		// selectedAttributesLinearLayout.setLayoutAnimation(controller);

		categoriesExpandableListView = (ExpandableListView) v.findViewById(R.id.categoriesExpandableListView);
		// categoriesExpandableListView.setLayoutAnimation(controller);

		categoriesTextView = (TextView) v.findViewById(R.id.categoriesTextView);

		listView = (ListView) v.findViewById(R.id.markalarListView);
		// listView.setLayoutAnimation(controller);

		scrollView1 = (ScrollView) v.findViewById(R.id.scrollView1);

		VerticalSeekBar verticalSeekBar = (VerticalSeekBar) v.findViewById(R.id.verticalSeekBar1);

		verticalSeekBar.setScrollView(scrollView1);

		/*
		 * expandableListView.setOnScrollListener(new OnScrollListener() {
		 * 
		 * @Override public void onScrollStateChanged(AbsListView view, int
		 * scrollState) {
		 * 
		 * }
		 * 
		 * @Override public void onScroll(AbsListView view, int
		 * firstVisibleItem, int visibleItemCount, int totalItemCount) {
		 * 
		 * } });
		 */

		try {

			/*
			 * scrollView1.setOnTouchListener(new View.OnTouchListener() {
			 * 
			 * @Override public boolean onTouch(View v, MotionEvent event) {
			 * 
			 * // Log.v(TAG,"PARENT TOUCH");
			 * listView.getParent().requestDisallowInterceptTouchEvent( false);
			 * expandableListView.getParent()
			 * .requestDisallowInterceptTouchEvent(false);
			 * categoriesExpandableListView.getParent()
			 * .requestDisallowInterceptTouchEvent(false);
			 * 
			 * return false;
			 * 
			 * }
			 * 
			 * });
			 */

			listView.setOnTouchListener(new View.OnTouchListener() {
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					// Log.v(TAG,"CHILD TOUCH");
					v.getParent().requestDisallowInterceptTouchEvent(true);
					return false;
				}
			});

			expandableListView.setOnScrollListener(new OnScrollListener() {

				@Override
				public void onScrollStateChanged(AbsListView view, int scrollState) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

				}
			});

			expandableListView.setOnTouchListener(new View.OnTouchListener() {
				@Override
				public boolean onTouch(View v, MotionEvent event) {

					int action = event.getAction();
					v.getParent().requestDisallowInterceptTouchEvent(dispatchMode);
					switch (action) {
					case MotionEvent.ACTION_DOWN:
						// Disallow ScrollView to intercept touch events.
						v.getParent().requestDisallowInterceptTouchEvent(dispatchMode);

						break;

					case MotionEvent.ACTION_UP:
						// Allow ScrollView to intercept touch events.
						v.getParent().requestDisallowInterceptTouchEvent(dispatchMode);

						break;
					}

					// Handle ListView touch events.
					v.onTouchEvent(event);
					return true;

				}
			});

			// when group expands//
			// Listview Group expanded listener
			expandableListView.setOnGroupExpandListener(new OnGroupExpandListener() {

				// int totalHeight = scrollView1.getChildAt(0).getHeight();

				@Override
				public void onGroupExpand(int groupPosition) {
					LinearLayout.LayoutParams param = (LinearLayout.LayoutParams) expandableListView.getLayoutParams();
					// param.height = (expandableListView.getChildCount() *
					// expandableListView.getHeight());

					int childrenCount = leftAttributeAdapter.getChildrenCount(groupPosition);
					param.height += leftAttributeAdapter.getChildrenCount(groupPosition) * expandableListView.getChildAt(0).getHeight();
					if (childrenCount > 2 && childrenCount < 10) {
						param.height += 50;
					} else if (childrenCount > 10)
						param.height += 150;
					expandableListView.setLayoutParams(param);
					expandableListView.refreshDrawableState();
					scrollView1.refreshDrawableState();

				}
			});

			expandableListView.setOnChildClickListener(new OnChildClickListener() {

				@Override
				public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
					Toast.makeText(getActivity(), " Child Clicked***" + this.toString(), Toast.LENGTH_SHORT).show();
					return false;
				}
			});

			expandableListView.setOnGroupCollapseListener(new OnGroupCollapseListener() {

				@Override
				public void onGroupCollapse(int groupPosition) {
					Toast.makeText(getActivity(), " Collapsed***" + this.toString(), Toast.LENGTH_SHORT).show();
					LinearLayout.LayoutParams param = (LinearLayout.LayoutParams) expandableListView.getLayoutParams();
					int childrenCount = leftAttributeAdapter.getChildrenCount(groupPosition);
					param.height -= leftAttributeAdapter.getChildrenCount(groupPosition) * expandableListView.getChildAt(0).getHeight();
					if (childrenCount > 2 && childrenCount < 10) {
						param.height -= 50;
					} else if (childrenCount > 10)
						param.height -= 150;
					expandableListView.setLayoutParams(param);
					expandableListView.refreshDrawableState();
					scrollView1.refreshDrawableState();

				}
			});

			categoriesExpandableListView.setOnTouchListener(new View.OnTouchListener() {
				@Override
				public boolean onTouch(View v, MotionEvent event) {

					v.getParent().requestDisallowInterceptTouchEvent(true);
					return false;
				}
			});

			ArrayList<Attribute> selectedAttributes = AppData.getInstance().getSelectedAttributes();
			ArrayList<Brand> selectedBrands = AppData.getInstance().getSelectedBrands();

			if (selectedBrands.size() > 0 || selectedAttributes.size() > 0
					|| AppData.getInstance().isQueriedCategoryCheckboxStatusHasSelectedCats()
					|| AppData.getInstance().isPriceSeekBarSelected())
				selectedAttributesLinearLayout.setVisibility(View.VISIBLE);
			else
				selectedAttributesLinearLayout.setVisibility(View.GONE);
			btnSecilenler = (ImageButton) v.findViewById(R.id.btnSecilenler);
			btnSecilenler.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {

					if (selectedAttributesExpandableListView.getVisibility() != View.VISIBLE)
						ViewUtil.expand(selectedAttributesExpandableListView);
					else
						ViewUtil.collapse(selectedAttributesExpandableListView);

					if (selectedBrandsExpandableListView.getVisibility() != View.VISIBLE)
						ViewUtil.expand(selectedBrandsExpandableListView);
					else
						ViewUtil.collapse(selectedBrandsExpandableListView);
					if (btnQueriedCategoryCheckboxStatusClear.getVisibility() != View.VISIBLE) {
						if (AppData.getInstance().isQueriedCategoryCheckboxStatusHasSelectedCats())
							btnQueriedCategoryCheckboxStatusClear.setVisibility(View.VISIBLE);
					} else {
						btnQueriedCategoryCheckboxStatusClear.setVisibility(View.GONE);
					}
					if (btnSelectedPricesClear.getVisibility() != View.VISIBLE) {

						if (AppData.getInstance().isPriceSeekBarSelected())
							btnSelectedPricesClear.setVisibility(View.VISIBLE);

					} else {
						btnSelectedPricesClear.setVisibility(View.GONE);
					}
				}
			});

			btnSecilenleriTemizle = (ImageButton) v.findViewById(R.id.btnSecilenleriTemizle);
			btnSecilenleriTemizle.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					ArrayList<Attribute> selectedAttributes = AppData.getInstance().getSelectedAttributes();
					ArrayList<Brand> selectedBrands = AppData.getInstance().getSelectedBrands();

					selectedAttributes.clear();
					selectedBrands.clear();

					clearQueriedCategoryCheckboxStatus();
					AppData.getInstance().setQueriedCategoryCheckboxStatusHasSelectedCats(false);
					AppData.getInstance().setSelectedMinPriceValue(-1);
					AppData.getInstance().setSelectedMaxPriceValue(-1);
					AppData.getInstance().setSelectedSeekBarStepSize(-1);
					AppData.getInstance().setPriceSeekBarSelected(false);

					String price_sql = "";
					String price_sql2 = "";

					price_sql = " 1 = 1 ";
					price_sql2 = " 1 = 1 ";

					Qry_parameters.Item qry_parameter = qry_parameters.getNewItem();
					Qry_parameters.Item qry_parameter2 = qry_parameters.getNewItem();

					qry_parameter.set_prmtr_name("selected_prices1");
					qry_parameter.set_prmtr_value(" " + price_sql + " ");

					qry_parameter2.set_prmtr_name("selected_prices2");
					qry_parameter2.set_prmtr_value(" " + price_sql2 + " ");

					try {
						qry_parametersDAO.updateQry_parameter(qry_parameter);
						qry_parametersDAO.updateQry_parameter(qry_parameter2);

					} catch (SQLException e) {

						e.printStackTrace();
					} catch (Exception e) {
						e.printStackTrace();
					}

					try {
						attemptReLoad();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			});

			btnSelectedPricesClear = (Button) v.findViewById(R.id.btnSelectedPricesClear);
			btnSelectedPricesClear.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					AppData.getInstance().setSelectedMinPriceValue(-1);
					AppData.getInstance().setSelectedMaxPriceValue(-1);
					AppData.getInstance().setSelectedSeekBarStepSize(-1);
					AppData.getInstance().setPriceSeekBarSelected(false);

					String price_sql = "";
					String price_sql2 = "";

					price_sql = " 1 = 1 ";
					price_sql2 = " 1 = 1 ";

					Qry_parameters.Item p_qry_parameter = qry_parameters.getNewItem();
					Qry_parameters.Item p_qry_parameter2 = qry_parameters.getNewItem();

					p_qry_parameter.set_prmtr_name("selected_prices1");
					p_qry_parameter.set_prmtr_value(" " + price_sql + " ");

					p_qry_parameter2.set_prmtr_name("selected_prices2");
					p_qry_parameter2.set_prmtr_value(" " + price_sql2 + " ");

					try {
						qry_parametersDAO.updateQry_parameter(p_qry_parameter);
						qry_parametersDAO.updateQry_parameter(p_qry_parameter2);

					} catch (SQLException e) {

						e.printStackTrace();
					} catch (Exception e) {
						e.printStackTrace();
					}

					try {
						attemptReLoad();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			});

			btnQueriedCategoryCheckboxStatusClear = (Button) v.findViewById(R.id.btnQueriedCategoryCheckboxStatusClear);
			btnQueriedCategoryCheckboxStatusClear.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					clearQueriedCategoryCheckboxStatus();
					AppData.getInstance().setQueriedCategoryCheckboxStatusHasSelectedCats(false);

					try {
						attemptReLoad();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			});

			btnFiltrele = (ImageButton) v.findViewById(R.id.btnFiltrele);
			btnFiltrele.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					try {
						attemptReLoad();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			});

			mLoginStatusView = v.findViewById(R.id.login_status);
			mLoginStatusMessageView = (TextView) v.findViewById(R.id.login_status_message);

			stokluCheckBox = (CheckBox) v.findViewById(R.id.stokluCheckBox);
			stokluCheckBox.setChecked(AppData.getInstance().getOnlyInStock());
			stokluCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

					buttonView.setEnabled(false);

					AppData.getInstance().setOnlyInStock(isChecked);

					Qry_parameters qry_parameters = new Qry_parameters();
					Qry_parameters.Item qry_parameter = qry_parameters.getNewItem();

					qry_parameter.set_prmtr_name("stokavail1");

					if (isChecked)
						qry_parameter.set_prmtr_value(" p.stok_flg = 1 ");
					else
						qry_parameter.set_prmtr_value("1 = 1");

					try {
						qry_parametersDAO.updateQry_parameter(qry_parameter);
					} catch (SQLException e) {

						e.printStackTrace();
					} catch (Exception e) {
						e.printStackTrace();
					}

					qry_parameter.set_prmtr_name("stokavail2");

					if (isChecked)
						qry_parameter.set_prmtr_value(" p1.stok_flg = 1 ");
					else
						qry_parameter.set_prmtr_value("1 = 1");

					try {
						qry_parametersDAO.updateQry_parameter(qry_parameter);
					} catch (SQLException e) {

						e.printStackTrace();
					} catch (Exception e) {
						e.printStackTrace();
					}

					/*
					 * if(p_from.equals("Search")){
					 * 
					 * setQueriedProducts(); }
					 * 
					 * Bundle arguments = new Bundle();
					 * arguments.putString("p_from", p_from);
					 * 
					 * ProductListWithAttributesActivity_ProductListFragment
					 * fragmentProductList = new
					 * ProductListWithAttributesActivity_ProductListFragment ();
					 * fragmentProductList.setArguments(arguments); getActivity
					 * ().getSupportFragmentManager().beginTransaction
					 * ().replace(R.id.product_list,
					 * fragmentProductList).commit();
					 * 
					 * 
					 * ProductListWithAttributesActivity_AttributesFragment
					 * fragmentLeftAttributes = new
					 * ProductListWithAttributesActivity_AttributesFragment ();
					 * fragmentLeftAttributes.setArguments(arguments);
					 * getActivity
					 * ().getSupportFragmentManager().beginTransaction
					 * ().replace(R.id.activity_list,
					 * fragmentLeftAttributes).commit();
					 */

					try {
						attemptReLoadIndependently();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					buttonView.setEnabled(true);
				}
			});

			minPriceTextView = (TextView) v.findViewById(R.id.minPriceTextView);
			maxPriceTextView = (TextView) v.findViewById(R.id.maxPriceTextView);

			layout = (LinearLayout) v.findViewById(R.id.seekBarLinearLayout);

			// /////////////////////////////////////////

			attemptLoad();

		} catch (Exception e) {

			ExceptionManager.setAppErrorLog("Error", "LeftAttributes onCreateView:" + e.getMessage(), "ANDROID_USER");
		}

		return v;
	}

	public void attemptLoad() {
		if (mLoadTask != null) {
			return;
		}

		mLoadTask = new LoadTask(getActivity());
		mLoadTask.execute((Void) null);

	}

	public void attemptReLoad() {
		if (mReLoadTask != null) {
			return;
		}

		mReLoadTask = new ReLoadTask(getActivity());
		mReLoadTask.execute((Void) null);

	}

	public void attemptReLoadIndependently() {
		if (mReLoadTaskIndependently != null) {
			return;
		}

		mReLoadTaskIndependently = new ReLoadTaskIndependently(getActivity());
		mReLoadTaskIndependently.execute((Void) null);

	}

	public class LoadTask extends AsyncTask<Void, Void, Boolean> {

		private Context context;
		Price pr_price;

		public LoadTask(Context c) {
			context = c;

		}

		@Override
		protected void onPreExecute() {

			showProgress(true);

			super.onPreExecute();
		}

		@Override
		protected Boolean doInBackground(Void... params) {

			try {

				Log.i("DEBUG_TAG", "LoadTask: B=" + new Date().getTime());

				int stepCount = 1;
				double fark = 0;
				double tmp_fark = 0;
				seekBarStepSize = 1;

				// //////////////////////////////////////////////////

				if (p_from.equals("Menu")) {

					if (AppData.getInstance().getSelectedMinPriceValue() > -1 || AppData.getInstance().getSelectedMaxPriceValue() > -1) {

						pr_price = new Price();
						pr_price.set_priceMin(AppData.getInstance().getSelectedMinPriceValue()
								* AppData.getInstance().getSelectedSeekBarStepSize());
						pr_price.set_priceMax(AppData.getInstance().getSelectedMaxPriceValue()
								* AppData.getInstance().getSelectedSeekBarStepSize());

					} else {

						pr_price = productsDAO.getProductMaxMinPrices();
					}
				} else {

					pr_price = productsDAO.getProductMaxMinPrices();
				}

				Log.i("DEBUG_TAG", "LoadTask: 1" + new Date().getTime());

				if (pr_price.get_priceMax() == pr_price.get_priceMin()) { // Eðer
																			// tek
																			// bir
																			// fiyat
																			// geldiyse...

					seekBarStepSize = (int) Math.round(pr_price.get_priceMax());

					stepCount = 1;
				} else {

					fark = pr_price.get_priceMax() - pr_price.get_priceMin();
					tmp_fark = fark / 100;

					if (tmp_fark < seekBarContStepSize_1) {
						seekBarStepSize = seekBarStepSize_1;
					} else if (tmp_fark < seekBarContStepSize_5) {
						seekBarStepSize = seekBarStepSize_1;
					} else if (tmp_fark < seekBarContStepSize_10) {
						seekBarStepSize = seekBarStepSize_5;
					} else if (tmp_fark < seekBarContStepSize_20) {
						seekBarStepSize = seekBarStepSize_10;
					} else if (tmp_fark < seekBarContStepSize_50) {
						seekBarStepSize = seekBarStepSize_20;
					} else if (tmp_fark < seekBarContStepSize_100) {
						seekBarStepSize = seekBarStepSize_50;
					} else {
						seekBarStepSize = seekBarStepSize_100;
					}

					stepCount = (int) Math.ceil(pr_price.get_priceMax() / seekBarStepSize);

					// seekBarStepSize = (int)( (pr_price.get_priceMax() -
					// pr_price.get_priceMin())/10);
				}

				if (seekBarStepSize == 0)
					seekBarStepSize = 1;

				// Log.i("RangeSeekBar", "seekBarStepSize=" + seekBarStepSize );

				min = (int) (pr_price.get_priceMin() / seekBarStepSize);
				max = stepCount;

				AppData.getInstance().setSelectedSeekBarStepSize(seekBarStepSize);
				AppData.getInstance().setSelectedMinPriceValue(min);
				AppData.getInstance().setSelectedMaxPriceValue(max);

				seekBar = new RangeSeekBar<Integer>(min, max, getActivity());

				if (AppData.getInstance().getSelectedMinPriceValue() > -1) {
					seekBar.setSelectedMinValue(AppData.getInstance().getSelectedMinPriceValue());
				}

				if (AppData.getInstance().getSelectedMaxPriceValue() > -1) {
					seekBar.setSelectedMaxValue(AppData.getInstance().getSelectedMaxPriceValue());
				}

				seekBar.setOnRangeSeekBarChangeListener(new OnRangeSeekBarChangeListener<Integer>() {

					@Override
					public void onRangeSeekBarValuesChanging(RangeSeekBar<?> bar, Integer minValue, Integer maxValue) {
						// handle changed range values
						// Log.i("RangeSeekBar",
						// "User selected new range values: MIN=" + minValue +
						// ", MAX=" + maxValue);

						View parent = (View) bar.getParent();

						if (minValue.compareTo(maxValue) == 0) {
							return;
						}

						TextView minPriceTextView = (TextView) parent.findViewById(R.id.minPriceTextView);
						TextView maxPriceTextView = (TextView) parent.findViewById(R.id.maxPriceTextView);

						minPriceTextView.setText(NumberUtil.formatNumber(minValue * seekBarStepSize));
						maxPriceTextView.setText(NumberUtil.formatNumber(maxValue * seekBarStepSize));

					}

					@Override
					public void onRangeSeekBarValuesChanged(RangeSeekBar<?> bar, Integer minValue, Integer maxValue) {
						// handle changed range values
						// Log.i("RangeSeekBar",
						// "User selected new range values: MIN=" + minValue +
						// ", MAX=" + maxValue);

						if (bar != null)
							bar.setEnabled(false);

						View parent = (View) bar.getParent();

						TextView minPriceTextView = (TextView) parent.findViewById(R.id.minPriceTextView);
						TextView maxPriceTextView = (TextView) parent.findViewById(R.id.maxPriceTextView);

						minPriceTextView.setText(NumberUtil.formatNumber(minValue * seekBarStepSize));
						maxPriceTextView.setText(NumberUtil.formatNumber(maxValue * seekBarStepSize));

						AppData.getInstance().setSelectedMinPriceValue(minValue);
						AppData.getInstance().setSelectedMaxPriceValue(maxValue);
						AppData.getInstance().setSelectedSeekBarStepSize(seekBarStepSize);
						AppData.getInstance().setPriceSeekBarSelected(true);

						String price_sql = "";
						String price_sql2 = "";

						price_sql = "  exists (select null from pr_prices pr where 1 = 1 and pr.price between " + minValue
								* seekBarStepSize + " and " + maxValue * seekBarStepSize + " and p.prod_id = pr.prod_id )";
						price_sql2 = " exists (select null from pr_prices pr where 1 = 1 and pr.price between " + minValue
								* seekBarStepSize + " and " + maxValue * seekBarStepSize + " and p1.prod_id = pr.prod_id )";

						Qry_parameters.Item qry_parameter = qry_parameters.getNewItem();
						Qry_parameters.Item qry_parameter2 = qry_parameters.getNewItem();

						qry_parameter.set_prmtr_name("selected_prices1");
						qry_parameter.set_prmtr_value(" " + price_sql + " ");

						qry_parameter2.set_prmtr_name("selected_prices2");
						qry_parameter2.set_prmtr_value(" " + price_sql2 + " ");

						try {
							qry_parametersDAO.updateQry_parameter(qry_parameter);
							qry_parametersDAO.updateQry_parameter(qry_parameter2);

						} catch (SQLException e) {

							e.printStackTrace();
						} catch (Exception e) {
							e.printStackTrace();
						}

						/*
						 * if(p_from.equals("Search")){
						 * 
						 * setQueriedProducts(); }
						 * 
						 * Bundle arguments = new Bundle();
						 * arguments.putString("p_from", p_from);
						 * 
						 * ProductListWithAttributesActivity_ProductListFragment
						 * fragmentProductList = new
						 * ProductListWithAttributesActivity_ProductListFragment
						 * (); fragmentProductList.setArguments(arguments);
						 * getActivity
						 * ().getSupportFragmentManager().beginTransaction
						 * ().replace(R.id.product_list,
						 * fragmentProductList).commit();
						 * 
						 * 
						 * 
						 * ProductListWithAttributesActivity_AttributesFragment
						 * fragmentLeftAttributes = new
						 * ProductListWithAttributesActivity_AttributesFragment
						 * (); fragmentLeftAttributes.setArguments(arguments);
						 * getActivity
						 * ().getSupportFragmentManager().beginTransaction
						 * ().replace(R.id.activity_list,
						 * fragmentLeftAttributes).commit();
						 */

						try {
							attemptReLoadIndependently();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						// Log.i("RangeSeekBar", "price_sql=" + price_sql );
					}
				});

				// //////////////////////////////////////////////////
				Log.i("DEBUG_TAG", "LoadTask: 2" + new Date().getTime());

				brandListAdapter = new BrandListAdapter(getActivity(), ProductListWithAttributesActivity_AttributesFragment.this);
				ArrayList<Brand> brands = new ArrayList<Brand>();

				brands = pr_categoriesDAO.getCategoryBrands();

				brandListAdapter.setBrands(brands);
				Log.i("DEBUG_TAG", "LoadTask: 3" + new Date().getTime());
				// /////////////////////////////////////////////////////////

				leftAttributeAdapter = new LeftAttributeAdapter(getActivity());

				attributes = pr_attributesDAO.getProductsAttributes();

				leftAttributeAdapter.setAttributes(attributes);

				// /////////////////////////////////////////////////////////////////////////
				if (p_from.equals("Search")) {

					categories = AppData.getInstance().getQueriedCategories();

					// categories = pr_categoriesDAO.getQueriedCategories();

				}
				// ////////////////////////////////////////////////////////////////////////
				Log.i("DEBUG_TAG", "LoadTask: 4" + new Date().getTime());

				selectedLeftAttributeAdapter = new SelectedLeftAttributeAdapter(getActivity(),
						ProductListWithAttributesActivity_AttributesFragment.this);

				selectedBrandListAdapter = new SelectedBrandListAdapter(getActivity(),
						ProductListWithAttributesActivity_AttributesFragment.this);

				Log.i("DEBUG_TAG", "LoadTask: A=" + new Date().getTime());

			} catch (Exception ex) {

				ExceptionManager.setAppErrorLog("Error", "LeftAttributes LoadTask doInBackground:" + ex.getMessage(), "ANDROID_USER");

				return false;
			}

			return true;
		}

		@Override
		protected void onPostExecute(final Boolean success) {
			mLoadTask = null;

			if (success) {

				Log.i("DEBUG_TAG", "LoadTask onPostExecute: B=" + new Date().getTime());

				// ///////////////////////////

				minPriceTextView.setText(NumberUtil.formatNumber(min * seekBarStepSize));
				maxPriceTextView.setText(NumberUtil.formatNumber(max * seekBarStepSize));

				if (AppData.getInstance().getSelectedMinPriceValue() > -1) {
					minPriceTextView.setText(NumberUtil.formatNumber(AppData.getInstance().getSelectedMinPriceValue() * seekBarStepSize));
				}

				if (AppData.getInstance().getSelectedMaxPriceValue() > -1) {
					maxPriceTextView.setText(NumberUtil.formatNumber(AppData.getInstance().getSelectedMaxPriceValue() * seekBarStepSize));
				}
				// //////////////////////////

				layout.addView(seekBar);

				// ////////////////////////////

				listView.setAdapter(brandListAdapter);
				Point p = ViewUtil.getScreenDimension(getActivity());

				if (p.y < 800)
					ViewUtil.setHeightofListView(listView, 100, p.y / 4);
				else
					ViewUtil.setHeightofListView(listView, 100, p.y / 4);

				// Toast.makeText(getActivity(), p.y +"",
				// Toast.LENGTH_LONG).show();

				// Toast.makeText(getActivity(),
				// getResources().getDisplayMetrics().density +"",
				// Toast.LENGTH_LONG).show();

				// ////////////////////////////

				expandableListView.setAdapter(leftAttributeAdapter);
				ViewUtil.setHeightofListView(expandableListView, 100, 0);
				// ///////////////////////////////

				if (p_from.equals("Menu")) {

					categoriesExpandableListView.setVisibility(View.GONE);
					categoriesTextView.setVisibility(View.GONE);

				} else {

					// kýsaca sadece tek bir kategory geliyorsa kategorileri
					// ekranda gösterme...
					if (categories.size() == 1 && categories.get(0).getSubCategories().size() == 1
							&& categories.get(0).getSubCategories().get(0).getSubCategories().size() == 1) {

						categoriesExpandableListView.setVisibility(View.GONE);
						categoriesTextView.setVisibility(View.GONE);
					} else {

						categoriesExpandableListView.setVisibility(View.VISIBLE);
						categoriesTextView.setVisibility(View.VISIBLE);

						CategoryLevel3Adapter adapter = new CategoryLevel3Adapter(context, categories);

						categoriesExpandableListView.setAdapter(adapter);

						for (int i = 0; i < adapter.getGroupCount(); i++)
							categoriesExpandableListView.expandGroup(i);

					}

				}

				// //////////////////////////////

				selectedAttributesExpandableListView.setAdapter(selectedLeftAttributeAdapter);

				// ////////////////////////////

				selectedBrandsExpandableListView.setAdapter(selectedBrandListAdapter);

				Log.i("DEBUG_TAG", "LoadTask onPostExecute: A=" + new Date().getTime());

				// ///////////////////////////

			}
			// ///////////////////////////////

			showProgress(false);

		}

		@Override
		protected void onCancelled() {
			mLoadTask = null;

			showProgress(false);
		}

	}

	private void setQueriedProducts() {

		try {

			String result = "";
			prod_ids = productsDAO.getSearchedProductList();

			for (int i = 0; i < prod_ids.size(); i++) {

				if (i == 0)
					result = prod_ids.get(i).toString();
				else
					result += "," + prod_ids.get(i).toString();

			}

			Qry_parameters.Item qry_parameter = qry_parameters.getNewItem();
			qry_parameter.set_prmtr_name("queried_prods1");
			qry_parameter.set_prmtr_value(" p.prod_id in ( " + result + ") ");

			Qry_parameters.Item qry_parameter1 = qry_parameters.getNewItem();
			qry_parameter1.set_prmtr_name("queried_prods2");
			qry_parameter1.set_prmtr_value(" p1.prod_id in ( " + result + ") ");

			qry_parametersDAO.updateQry_parameter(qry_parameter);
			qry_parametersDAO.updateQry_parameter(qry_parameter1);

			ArrayList<Category> queriedCategories = pr_categoriesDAO.getQueriedCategories();
			AppData.getInstance().setQueriedCategories(queriedCategories);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public class ReLoadTask extends AsyncTask<Void, Void, Boolean> {

		private Context context;

		public ReLoadTask(Context c) {
			context = c;

		}

		@Override
		protected void onPreExecute() {

			showProgress(true);
			super.onPreExecute();
		}

		@Override
		protected Boolean doInBackground(Void... params) {

			try {

				doFilter();

			} catch (Exception ex) {

				ExceptionManager.setAppErrorLog("Error", "LeftAttributes ReLoadTask doInBackground:" + ex.getMessage(), "ANDROID_USER");

				return false;
			}

			return true;
		}

		@Override
		protected void onPostExecute(final Boolean success) {
			mReLoadTask = null;

			showProgress(false);
		}

		@Override
		protected void onCancelled() {
			mReLoadTask = null;

			showProgress(false);
		}

	}

	public class ReLoadTaskIndependently extends AsyncTask<Void, Void, Boolean> {

		private Context context;

		public ReLoadTaskIndependently(Context c) {
			context = c;

		}

		@Override
		protected void onPreExecute() {

			showProgress(true);
			super.onPreExecute();
		}

		@Override
		protected Boolean doInBackground(Void... params) {

			try {

				doFilterIndependently();

			} catch (Exception ex) {

				ExceptionManager.setAppErrorLog("Error", "LeftAttributes ReLoadTaskIndependently doInBackground:" + ex.getMessage(),
						"ANDROID_USER");

				return false;
			}

			return true;
		}

		@Override
		protected void onPostExecute(final Boolean success) {
			mReLoadTask = null;

			showProgress(false);
		}

		@Override
		protected void onCancelled() {
			mReLoadTask = null;

			showProgress(false);
		}

	}

	public void doFilter() throws Exception {

		// ///////////////////////////////////////////Attributes/////////////////////////////////////////////////////

		String atribute_sql = "";
		String atribute_sql2 = "";
		ArrayList<Attribute> selectedAttributes = AppData.getInstance().getSelectedAttributes();

		for (int i = 0; i < selectedAttributes.size(); i++) {// attributeCheckboxStatus
																// larý
																// alýyorum..
																// ediyorum...

			ArrayList<AttributeValue> al = selectedAttributes.get(i).getAttribute_values();

			String atribute_sql_tmp = " p.prod_id in( select prod_id from pr_attribute_values av where av.att_id = "
					+ selectedAttributes.get(i).get_att_id() + " AND av.value in(";
			String atribute_sql_tmp_inner = "";

			for (int j = 0; j < al.size(); j++) {

				if (StringUtil.isNothing(atribute_sql_tmp_inner))
					atribute_sql_tmp_inner = "'" + selectedAttributes.get(i).getAttribute_values().get(j).get_value() + "'";
				else
					atribute_sql_tmp_inner += ", '" + selectedAttributes.get(i).getAttribute_values().get(j).get_value() + "'";

			}

			if (!StringUtil.isNothing(atribute_sql_tmp_inner)) {
				atribute_sql_tmp += atribute_sql_tmp_inner + "))";

				// and prod_id
				// in(
				// select
				// prod_id
				// from
				// pr_attribute_values
				// av where
				// av.att_id
				// = 3752
				// AND (Lg)

				if (StringUtil.isNothing(atribute_sql))
					atribute_sql += atribute_sql_tmp;
				else
					atribute_sql += " AND " + atribute_sql_tmp;

				// Log.i("chk", "atribute_sql_tmp_inner: " +
				// atribute_sql_tmp_inner);
			}

			String atribute_sql_tmp2 = " p1.prod_id in( select prod_id from pr_attribute_values av where av.att_id = "
					+ selectedAttributes.get(i).get_att_id() + " AND av.value in(";
			String atribute_sql_tmp_inner2 = "";

			for (int j = 0; j < al.size(); j++) {

				if (StringUtil.isNothing(atribute_sql_tmp_inner2))
					atribute_sql_tmp_inner2 = "'" + selectedAttributes.get(i).getAttribute_values().get(j).get_value() + "'";
				else
					atribute_sql_tmp_inner2 += ", '" + selectedAttributes.get(i).getAttribute_values().get(j).get_value() + "'";

			}

			if (!StringUtil.isNothing(atribute_sql_tmp_inner2)) {
				atribute_sql_tmp2 += atribute_sql_tmp_inner2 + "))";

				// and
				// prod_id
				// in(
				// select
				// prod_id
				// from
				// pr_attribute_values
				// av where
				// av.att_id
				// = 3752
				// AND (Lg)
				if (StringUtil.isNothing(atribute_sql2))
					atribute_sql2 += atribute_sql_tmp2;
				else
					atribute_sql2 += " AND " + atribute_sql_tmp2;

			}

		}

		// Log.i("chk", "atribute_sql: " + atribute_sql);

		Qry_parameters.Item qry_parameter = qry_parameters.getNewItem();
		Qry_parameters.Item qry_parameter2 = qry_parameters.getNewItem();

		if (!StringUtil.isNothing(atribute_sql)) {

			qry_parameter.set_prmtr_name("selected_attributes1");
			qry_parameter.set_prmtr_value(" " + atribute_sql + " ");

			qry_parameter2.set_prmtr_name("selected_attributes2");
			qry_parameter2.set_prmtr_value(" " + atribute_sql2 + " ");

		} else {

			qry_parameter.set_prmtr_name("selected_attributes1");
			qry_parameter.set_prmtr_value(" 1 = 1 ");

			qry_parameter2.set_prmtr_name("selected_attributes2");
			qry_parameter2.set_prmtr_value(" 1 = 1 ");

		}

		qry_parametersDAO.updateQry_parameter(qry_parameter);
		qry_parametersDAO.updateQry_parameter(qry_parameter2);

		// ///////////////////////////////// Markalar
		// ///////////////////////////////////

		ArrayList<Brand> selectedBrands = AppData.getInstance().getSelectedBrands();

		String brand_sql = "";
		String brand_sql2 = "";
		String brand_sql_tmp = " p.brand  in(";
		String brand_sql_tmp_inner = "";

		for (int i = 0; i < selectedBrands.size(); i++) {// brandCheckboxStatus
															// larý alýyorum..
															// ediyorum...

			if (StringUtil.isNothing(brand_sql_tmp_inner))
				brand_sql_tmp_inner = "'" + selectedBrands.get(i).get_name() + "'";
			else
				brand_sql_tmp_inner += ", '" + selectedBrands.get(i).get_name() + "'";

		}

		if (!StringUtil.isNothing(brand_sql_tmp_inner)) {
			brand_sql_tmp += brand_sql_tmp_inner + ")";// and prod_id in( select
														// prod_id from
														// pr_attribute_values
														// av where av.att_id =
														// 3752 AND (Lg)
			brand_sql = brand_sql_tmp;
			// Log.i("chk", "brand_sql_tmp_inner: " + brand_sql_tmp_inner);

		}

		String brand_sql_tmp2 = " p1.brand  in(";
		String brand_sql_tmp_inner2 = "";

		for (int i = 0; i < selectedBrands.size(); i++) {// brandCheckboxStatus
															// larý alýyorum..
															// ediyorum...

			if (StringUtil.isNothing(brand_sql_tmp_inner2))
				brand_sql_tmp_inner2 = "'" + selectedBrands.get(i).get_name() + "'";
			else
				brand_sql_tmp_inner2 += ", '" + selectedBrands.get(i).get_name() + "'";

		}

		if (!StringUtil.isNothing(brand_sql_tmp_inner2)) {
			brand_sql_tmp2 += brand_sql_tmp_inner2 + ")";// and prod_id in(
															// select prod_id
															// from
															// pr_attribute_values
															// av where
															// av.att_id = 3752
															// AND (Lg)
			brand_sql2 = brand_sql_tmp2;

		}

		Qry_parameters.Item b_qry_parameter = qry_parameters.getNewItem();
		Qry_parameters.Item b_qry_parameter2 = qry_parameters.getNewItem();

		if (!StringUtil.isNothing(brand_sql)) {

			b_qry_parameter.set_prmtr_name("selected_brands1");
			b_qry_parameter.set_prmtr_value(" " + brand_sql + " ");

			b_qry_parameter2.set_prmtr_name("selected_brands2");
			b_qry_parameter2.set_prmtr_value(" " + brand_sql2 + " ");

		} else {

			b_qry_parameter.set_prmtr_name("selected_brands1");
			b_qry_parameter.set_prmtr_value(" 1 = 1 ");

			b_qry_parameter2.set_prmtr_name("selected_brands2");
			b_qry_parameter2.set_prmtr_value(" 1 = 1 ");

		}

		qry_parametersDAO.updateQry_parameter(b_qry_parameter);
		qry_parametersDAO.updateQry_parameter(b_qry_parameter2);

		// //////////////////////////////////////////////Categories////////////////////////////////

		if (p_from.equals("Search")) {

			String selectedCatIds = "";

			ArrayList<CategoryCheckboxStatus> queriedCategoryCheckboxStatus = AppData.getInstance().getQueriedCategoryCheckboxStatus();

			for (int i = 0; i < queriedCategoryCheckboxStatus.size(); i++) {

				if (queriedCategoryCheckboxStatus.get(i).isChecked()) {// iþaretliyse
																		// altýndakileri
																		// komple
																		// al...

					selectedCatIds += "," + queriedCategoryCheckboxStatus.get(i).getCat_id();

					for (int j = 0; j < queriedCategoryCheckboxStatus.get(i).getSubCategoryCheckboxStatus().size(); j++) {

						selectedCatIds += "," + queriedCategoryCheckboxStatus.get(i).getSubCategoryCheckboxStatus().get(j).getCat_id();

						for (int k = 0; k < queriedCategoryCheckboxStatus.get(i).getSubCategoryCheckboxStatus().get(j)
								.getSubCategoryCheckboxStatus().size(); k++) {

							selectedCatIds += ","
									+ queriedCategoryCheckboxStatus.get(i).getSubCategoryCheckboxStatus().get(j)
											.getSubCategoryCheckboxStatus().get(k).getCat_id();

						}

					}

				}

				else {

					for (int j = 0; j < queriedCategoryCheckboxStatus.get(i).getSubCategoryCheckboxStatus().size(); j++) {

						if (queriedCategoryCheckboxStatus.get(i).getSubCategoryCheckboxStatus().get(j).isChecked()) {// iþaretliyse
																														// altýndakileri
																														// komple
																														// al...

							selectedCatIds += "," + queriedCategoryCheckboxStatus.get(i).getSubCategoryCheckboxStatus().get(j).getCat_id();

							for (int k = 0; k < queriedCategoryCheckboxStatus.get(i).getSubCategoryCheckboxStatus().get(j)
									.getSubCategoryCheckboxStatus().size(); k++) {

								selectedCatIds += ","
										+ queriedCategoryCheckboxStatus.get(i).getSubCategoryCheckboxStatus().get(j)
												.getSubCategoryCheckboxStatus().get(k).getCat_id();

							}

						} else {

							for (int k = 0; k < queriedCategoryCheckboxStatus.get(i).getSubCategoryCheckboxStatus().get(j)
									.getSubCategoryCheckboxStatus().size(); k++) {

								if (queriedCategoryCheckboxStatus.get(i).getSubCategoryCheckboxStatus().get(j)
										.getSubCategoryCheckboxStatus().get(k).isChecked()) {// iþaretliyse
																								// al...
									selectedCatIds += ","
											+ queriedCategoryCheckboxStatus.get(i).getSubCategoryCheckboxStatus().get(j)
													.getSubCategoryCheckboxStatus().get(k).getCat_id();
								}
							}

						}

					}

				}

			}

			if (!StringUtil.isNothing(selectedCatIds)) {
				if (selectedCatIds.startsWith(","))
					selectedCatIds = selectedCatIds.substring(1);
			}

			AppData.getInstance().setSelectedCatIds(selectedCatIds);

			Qry_parameters.Item c_qry_parameter = qry_parameters.getNewItem();
			Qry_parameters.Item c_qry_parameter2 = qry_parameters.getNewItem();

			if (!StringUtil.isNothing(selectedCatIds)) {

				c_qry_parameter.set_prmtr_name("cat_id1");
				c_qry_parameter.set_prmtr_value(" p.cat_id in (" + selectedCatIds + ") ");

				c_qry_parameter2.set_prmtr_name("cat_id2");
				c_qry_parameter2.set_prmtr_value(" p1.cat_id in (" + selectedCatIds + ") ");

				AppData.getInstance().setQueriedCategoryCheckboxStatusHasSelectedCats(true);

			} else {

				c_qry_parameter.set_prmtr_name("cat_id1");
				c_qry_parameter.set_prmtr_value(" 1 = 1 ");

				c_qry_parameter2.set_prmtr_name("cat_id2");
				c_qry_parameter2.set_prmtr_value(" 1 = 1 ");

				AppData.getInstance().setQueriedCategoryCheckboxStatusHasSelectedCats(false);

			}

			qry_parametersDAO.updateQry_parameter(c_qry_parameter);
			qry_parametersDAO.updateQry_parameter(c_qry_parameter2);

			if (p_from.equals("Search")) {

				setQueriedProducts();
			}

		}

		Bundle arguments = new Bundle();
		arguments.putString("p_from", p_from);

		ProductListWithAttributesActivity_ProductListFragment fragmentProductList = new ProductListWithAttributesActivity_ProductListFragment();
		fragmentProductList.setArguments(arguments);
		((ProductListWithAttributesActivity) getActivity()).getSupportFragmentManager().beginTransaction()
				.replace(R.id.product_list, fragmentProductList).commit();

		ProductListWithAttributesActivity_AttributesFragment fragmentLeftAttributes = new ProductListWithAttributesActivity_AttributesFragment();
		fragmentLeftAttributes.setArguments(arguments);
		((ProductListWithAttributesActivity) getActivity()).getSupportFragmentManager().beginTransaction()
				.replace(R.id.activity_list, fragmentLeftAttributes).commit();

	}

	public void doFilterIndependently() throws Exception {

		if (p_from.equals("Search")) {

			setQueriedProducts();
		}

		Bundle arguments = new Bundle();
		arguments.putString("p_from", p_from);

		ProductListWithAttributesActivity_ProductListFragment fragmentProductList = new ProductListWithAttributesActivity_ProductListFragment();
		fragmentProductList.setArguments(arguments);
		((ProductListWithAttributesActivity) getActivity()).getSupportFragmentManager().beginTransaction()
				.replace(R.id.product_list, fragmentProductList).commit();

		ProductListWithAttributesActivity_AttributesFragment fragmentLeftAttributes = new ProductListWithAttributesActivity_AttributesFragment();
		fragmentLeftAttributes.setArguments(arguments);
		((ProductListWithAttributesActivity) getActivity()).getSupportFragmentManager().beginTransaction()
				.replace(R.id.activity_list, fragmentLeftAttributes).commit();

	}

	private void clearQueriedCategoryCheckboxStatus() {

		AppData.getInstance().setSelectedCatIds("");

		ArrayList<CategoryCheckboxStatus> queriedCategoryCheckboxStatus = AppData.getInstance().getQueriedCategoryCheckboxStatus();

		for (int i = 0; i < queriedCategoryCheckboxStatus.size(); i++) {

			queriedCategoryCheckboxStatus.get(i).setChecked(false);

			for (int j = 0; j < queriedCategoryCheckboxStatus.get(i).getSubCategoryCheckboxStatus().size(); j++) {

				queriedCategoryCheckboxStatus.get(i).getSubCategoryCheckboxStatus().get(j).setChecked(false);

				for (int k = 0; k < queriedCategoryCheckboxStatus.get(i).getSubCategoryCheckboxStatus().get(j)
						.getSubCategoryCheckboxStatus().size(); k++) {

					queriedCategoryCheckboxStatus.get(i).getSubCategoryCheckboxStatus().get(j).getSubCategoryCheckboxStatus().get(k)
							.setChecked(false);

				}

			}

		}

	}

}
