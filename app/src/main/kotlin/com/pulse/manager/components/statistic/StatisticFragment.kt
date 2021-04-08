package com.pulse.manager.components.statistic

import by.kirich1409.viewbindingdelegate.viewBinding
import com.pulse.manager.R
import com.pulse.manager.core.base.fragment.BaseToolbarFragment
import com.pulse.manager.databinding.FragmentStatisticBinding
import org.koin.core.component.KoinApiExtension

@KoinApiExtension
class StatisticFragment : BaseToolbarFragment<StatisticViewModel>(R.layout.fragment_statistic, StatisticViewModel::class) {

    private val binding by viewBinding(FragmentStatisticBinding::bind)

    override fun initUI() = with(binding) {
        showBackButton()

        mtvRatingValue.text = getString(R.string.ratingHolder, 4.9f)
        mtvRecommendationValue.text = getString(R.string.recommendation_holder, 122)
        mtvOpenedChatsValue.text = getString(R.string.opened_chats_holder, 98)
        mtvTodayValue.text = "450 ₸" // TODO currency formatter
        mtvWeekValue.text = "2350 ₸" // TODO currency formatter
        mtvMonthValue.text = "16670 ₸" // TODO currency formatter
    }
}