package com.pulse.manager.components.product

import androidx.navigation.fragment.navArgs
import androidx.paging.ExperimentalPagingApi
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.pulse.manager.R
import com.pulse.manager.components.product.adapter.ProductsImageAdapter
import com.pulse.manager.core.extensions.*
import com.pulse.manager.databinding.FragmentProductBinding
import com.pulse.manager.util.ColorFilterUtil.blackWhiteFilter
import org.koin.core.component.KoinApiExtension

@KoinApiExtension
class ProductFragment: BaseProductFragment<ProductViewModel>(R.layout.fragment_product, ProductViewModel::class) {

    private val args: ProductFragmentArgs by navArgs()
    private val binding by viewBinding(FragmentProductBinding::bind)

    @ExperimentalPagingApi
    override fun initUI() = with(binding) {
        showBackButton()
        toolbar.toolbar.title = args.product.rusName

        with(layoutProductCardImagePager) {
            vpImage.adapter = ProductsImageAdapter(args.product.pictures)
            TabLayoutMediator(tabsIndicator, vpImage) { _, _ -> }.attach()
        }

        setProductInfo()

        with(layoutProductCardMainInfo) {
            mcvAnalog.mockToast()
            mcvCategory.mockToast()
        }
        with(layoutProductCardAdditionalInfo) {
            mcvInstruction.mockToast()
        }
    }

    @ExperimentalPagingApi
    private fun setProductInfo() = with(args.product) {
        with(binding.layoutProductCardMainInfo) {
            mtvTitle.setTextHtml(rusName)
            mtvSubtitle.setTextHtml(releaseForm)
            mtvManufacture.text = getFullManufacture

            aggregation?.let {
                mtvPriceTo.text = getString(R.string.price, aggregation?.maxPrice?.formatPrice())
                mtvPriceFrom.text = getString(R.string.price, aggregation?.minPrice?.formatPrice())
            } ?: run {
                binding.layoutProductCardImagePager.ivDetailAbsent.colorFilter = blackWhiteFilter
            }
            groupPriceFields.visibleOrGone(aggregation != null)
            mtvPriceUnavailable.visibleOrGone(aggregation == null)
            mtvAnalog.text = substance
            mcvAnalog.animateVisibleOrGoneIfNot(substance != null)
            ivCategory.setImageResource(R.drawable.ic_vaccine)
            mtvCategoryDescription.text = category
        }
        binding.layoutProductCardAdditionalInfo.mtvDescriptionText.setTextHtml(description)
    }
}