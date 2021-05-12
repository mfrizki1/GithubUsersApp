package id.calocallo.githubusersapp.adapter

import android.content.Context
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import id.calocallo.githubusersapp.R
import id.calocallo.githubusersapp.model.UsersDto
import id.calocallo.githubusersapp.utils.Constants
import kotlinx.android.synthetic.main.item_user.view.*
import kotlinx.android.synthetic.main.pb_loading.view.*
import java.util.*
import kotlin.collections.ArrayList

class UsersAdapter(val listUser: ArrayList<UsersDto>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>(), Filterable {

    var filterList = ArrayList<UsersDto>()

    init {
        filterList = listUser as ArrayList<UsersDto>
    }

    lateinit var mcontext: Context
    lateinit var onClick: (UsersDto) -> Unit

    fun setOnClickItemListener(onClick: (UsersDto) -> Unit) {
        this.onClick = onClick
    }

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    class LoadingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    fun addData(dataViews: MutableList<UsersDto>) {
        this.filterList.addAll(dataViews)
        notifyDataSetChanged()
    }

    fun getItemAtPosition(position: Int): UsersDto? {
        return filterList[position]
    }

    fun addLoadingView() {
        //add loading item
        Handler(Looper.getMainLooper()).post {
            filterList.add(UsersDto())
            notifyItemInserted(listUser.size - 1)
        }
    }

    fun removeLoadingView() {
        //Remove loading item
        if (filterList.size != 0) {
            filterList.removeAt(listUser.size - 1)
            notifyItemRemoved(listUser.size)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        mcontext = parent.context
        return if (viewType == Constants.VIEW_TYPE_ITEM) {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
            ItemViewHolder(view)
        } else {
            val view =
                LayoutInflater.from(mcontext).inflate(R.layout.pb_loading, parent, false)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                view.progressBar.indeterminateDrawable.colorFilter =
                    BlendModeColorFilter(Color.WHITE, BlendMode.SRC_ATOP)
            } else {
                view.progressBar.indeterminateDrawable.setColorFilter(
                    Color.WHITE,
                    PorterDuff.Mode.MULTIPLY
                )
            }
            LoadingViewHolder(view)
        }
    }

    override fun getItemCount(): Int {
        return filterList.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (listUser[position] == null) {
            Constants.VIEW_TYPE_LOADING
        } else {
            Constants.VIEW_TYPE_ITEM
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder.itemViewType == Constants.VIEW_TYPE_ITEM) {
            holder.itemView.txt_users.text = filterList[position]?.login
            Glide.with(mcontext).load(filterList[position].avatar_url).circleCrop().fitCenter()
                .into(holder.itemView.img_users)
            holder.itemView.setOnClickListener {
                onClick(filterList[position])
            }
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                if (charSearch.isEmpty()) {
                    filterList = listUser as ArrayList<UsersDto>
                } else {
                    val resultList = ArrayList<UsersDto>()
                    for (row in listUser) {
                        if (row.toString().toLowerCase(Locale.ROOT)
                                .contains(charSearch.toLowerCase(Locale.ROOT))
                        ) {
                            resultList.add(row)
                        }
                    }
                    filterList = resultList
                }
                val filterResult = FilterResults()
                filterResult.values = filterList
                return filterResult
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filterList = results?.values as ArrayList<UsersDto>
                notifyDataSetChanged()
            }

        }

    }

}