package com.pharmacy.manager.components.product

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.navArgs
import com.google.android.material.tabs.TabLayoutMediator
import com.pharmacy.manager.R
import com.pharmacy.manager.components.product.adapter.ProductsImageAdapter
import com.pharmacy.manager.core.extensions.*
import com.pharmacy.manager.util.ColorFilterUtil.blackWhiteFilter
import kotlinx.android.synthetic.main.layout_product_card_additional_info.*
import kotlinx.android.synthetic.main.layout_product_card_image_pager.*
import kotlinx.android.synthetic.main.layout_product_card_main_info.*

class ProductFragment(private val viewModel: ProductViewModel) : BaseProductFragment<ProductViewModel>(R.layout.fragment_product, viewModel) {

    private val args: ProductFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showBackButton()
        toolbar?.title = args.product.rusName

        with(productImagePager) {
            adapter = ProductsImageAdapter(args.product.pictures)
            TabLayoutMediator(productImagePagerIndicator, this) { _, _ -> }.attach()
        }

        setProductInfo()

        mcvAnalog.setDebounceOnClickListener { onAnalog() }
        mcvCategory.setDebounceOnClickListener { onCategory() }
        mcvInstruction.setDebounceOnClickListener { onInstruction() }
    }

    private fun onAnalog() = requireContext().toast(getString(R.string.expectSoonMock))

    private fun onCategory() = requireContext().toast("TODO: Category")

    private fun onInstruction() = requireContext().toast(getString(R.string.expectSoonMock))

    private fun setProductInfo() = with(args.product) {
        tvTitle.setTextHtml(rusName)
        tvSubTitle.setTextHtml(releaseForm)
        tvManufacture.text = getFullManufacture

        aggregation?.let {
            tvPriceTo.text = getString(R.string.price, aggregation?.maxPrice?.formatPrice())
            tvPriceFrom.text = getString(R.string.price, aggregation?.minPrice?.formatPrice())
        } ?: run {
            ivProductDetailAbsent.colorFilter = blackWhiteFilter
        }
        groupPriceFields.visibleOrGone(aggregation != null)
        tvPriceUnavailable.visibleOrGone(aggregation == null)

        tvAnalog.text = substance

        ivCategory.setImageResource(R.drawable.ic_vaccine)
        tvCategoryDes.text = category

        mtvDescriptionText.setTextHtml(description)
    }
}