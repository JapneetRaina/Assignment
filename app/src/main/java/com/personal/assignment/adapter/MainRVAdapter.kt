package com.personal.assignment.adapter

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.personal.assignment.R
import com.personal.assignment.databinding.CarItemLayoutBinding
import com.personal.assignment.model.Car
import java.util.Locale

class MainRVAdapter(private val context: Context, private var cars: List<Car>) :
    Adapter<AssignedRVViewHolder>(), Filterable {
    private var filteredList: List<Car> = cars
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AssignedRVViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding: CarItemLayoutBinding =
            DataBindingUtil.inflate(inflater, R.layout.car_item_layout, parent, false)
        return AssignedRVViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return filteredList.size
    }

    override fun onBindViewHolder(holder: AssignedRVViewHolder, position: Int) {
        if (position < filteredList.size) {
            val car = filteredList[position]
            holder.bind(car)
            val formattedCons = formatListAsBulletPoints(car.cons_list, "Cons :")
            val formattedPros = formatListAsBulletPoints(car.pros_list, "Pros :")
            val carImageResourceId = car.car_image?.let { getCarImageResourceId(it) }
            val carImageDrawable = if (carImageResourceId != 0) {
                if (carImageResourceId != null) {
                    ContextCompat.getDrawable(context, carImageResourceId)
                } else {
                    null
                }
            } else {
                null
            }
            holder.binding.apply {
                carImage.setImageDrawable(carImageDrawable)
                mainCV.visibility = if (cars.contains(car)) View.VISIBLE else View.GONE
                carDescription.setOnClickListener {
                    if (!descriptionLL.isVisible) {
                        descriptionLL.visibility = View.VISIBLE
                        carPros.text = formattedPros
                        carCons.text = formattedCons
                        val newDrawable: Drawable? =
                            holder.itemView.context.getDrawable(R.drawable.baseline_arrow_circle_up_24)
                        carDescription.setCompoundDrawablesWithIntrinsicBounds(
                            null,
                            null,
                            newDrawable,
                            null
                        )
                    } else {
                        descriptionLL.visibility = View.GONE
                        val newDrawable: Drawable? =
                            holder.itemView.context.getDrawable(R.drawable.baseline_arrow_circle_down_24)
                        carDescription.setCompoundDrawablesWithIntrinsicBounds(
                            null,
                            null,
                            newDrawable,
                            null
                        )
                    }
                }
            }
            holder.bind(car)

        } else {
            null
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val queryString = constraint?.toString()?.toLowerCase(Locale.getDefault())
                val filterResults = FilterResults()
                filterResults.values = if (queryString.isNullOrEmpty()) {
                    cars
                } else {
                    cars.filter { car ->
                        car.make.toLowerCase(Locale.getDefault()).contains(queryString)
                    }
                }
                return filterResults
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredList = results?.values as? List<Car> ?: emptyList()
                this@MainRVAdapter.filteredList = filteredList
                notifyDataSetChanged()
            }
        }
    }
}

private fun getCarImageResourceId(carImageFileName: String): Int {
    val carImageResourceIds = mapOf(
        "bmw1.jpg" to R.drawable.bmw1,
        "jaguar.jpg" to R.drawable.jaguar,
        "maruti_fronx.jpg" to R.drawable.maruti_fronx,
        "honda_jazz.jpg" to R.drawable.honda_jazz,
        "ford.jpg" to R.drawable.ford
    )
    return carImageResourceIds[carImageFileName] ?: 0
}

fun formatListAsBulletPoints(list: List<String>, prefix: String): String {
    val builder = StringBuilder()
    if (list.isNotEmpty()) {
        builder.append(prefix).append("\n")
    }
    for ((index, item) in list.withIndex()) {
        if (item.isNotEmpty()) {
            builder.append("• ")
            builder.append(item).append("\n")
        }
    }
    return builder.toString()
}

class AssignedRVViewHolder(val binding: CarItemLayoutBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(car: Car) {
        binding.car = car
        binding.executePendingBindings()
    }
}
