package com.pandulapeter.beagle.appDemo.feature.shared

import android.os.Bundle
import android.view.View
import androidx.annotation.CallSuper
import androidx.annotation.StringRes
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pandulapeter.beagle.Beagle
import com.pandulapeter.beagle.appDemo.BR
import com.pandulapeter.beagle.appDemo.R
import com.pandulapeter.beagle.appDemo.databinding.FragmentListBinding
import com.pandulapeter.beagle.appDemo.feature.shared.list.BaseAdapter
import com.pandulapeter.beagle.appDemo.feature.shared.list.ListItem
import com.pandulapeter.beagle.appDemo.utils.observe
import com.pandulapeter.beagle.common.contracts.module.Module
import com.pandulapeter.beagle.utils.extensions.colorResource
import com.pandulapeter.beagle.utils.extensions.waitForPreDraw

abstract class ListFragment<VM : ListViewModel<LI>, LI : ListItem>(
    @StringRes protected val titleResourceId: Int
) : BaseFragment<FragmentListBinding>(R.layout.fragment_list) {

    protected abstract val viewModel: VM

    protected abstract fun getBeagleModules(): List<Module<*>>

    protected abstract fun createAdapter(): BaseAdapter<LI>

    protected open fun createLayoutManager(): RecyclerView.LayoutManager = LinearLayoutManager(context)

    fun blockGestures() {
        try {
            binding.recyclerView.shouldBlockGestures = { true }
        } catch (_: IllegalStateException) {
        }
    }

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.setVariable(BR.viewModel, viewModel)
        binding.root.setBackgroundColor(requireContext().colorResource(android.R.attr.windowBackground))
        binding.appBar.setup(
            titleResourceId,
            (parentFragment?.childFragmentManager?.backStackEntryCount ?: 0) <= 1,
            requireActivity()
        )
        setupRecyclerView()
        refreshBeagle()
        setupEdgeToEdge()
    }

    override fun onDestroyView() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.appBar, null)
        super.onDestroyView()
    }

    protected fun refreshBeagle(clearIfEmpty: Boolean = false) = getBeagleModules().let { modules ->
        if (modules.isNotEmpty() || clearIfEmpty) {
            Beagle.set(*modules.toTypedArray())
        }
    }

    private fun setupRecyclerView() {
        val listAdapter =
            createAdapter().also { it.blockGestures = { binding.recyclerView.shouldBlockGestures = { true } } }
        viewModel.items.observe(owner = viewLifecycleOwner) { listAdapter.submitList(it, ::onListUpdated) }
        binding.recyclerView.run {
            shouldBlockGestures = { true }
            adapter = listAdapter
            layoutManager = createLayoutManager()
            setHasFixedSize(true)
            waitForPreDraw { postDelayed({ startPostponedEnterTransition() }, 100) }
        }
    }

    private fun setupEdgeToEdge() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.appBar) { _, insets ->
            binding.appBar.updateTopInset(insets.getInsets(WindowInsetsCompat.Type.systemBars()).top)
            insets
        }
    }

    private fun onListUpdated() {
        try {
            binding.appBar.run {
                postDelayed(
                    {
                        try {
                            isLifted = binding.recyclerView.computeVerticalScrollOffset() != 0
                            binding.recyclerView.shouldBlockGestures = { false }
                        } catch (_: IllegalStateException) {
                        }
                    }, 300
                )
            }
        } catch (_: IllegalStateException) {
        }
    }
}