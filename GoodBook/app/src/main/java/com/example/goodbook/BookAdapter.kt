package com.example.goodbook

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView

/**
 * Adapter for the [RecyclerView] in [MainActivity].
 */
class BookAdapter :
    RecyclerView.Adapter<BookAdapter.BookViewHolder>() {

    // Generates a list of books
    private val list = ('0').rangeTo('4').toList()

    /**
     * Provides a reference for the views needed to display items in your list.
     */
    class BookViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val button = view.findViewById<Button>(R.id.button_item)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    /**
     * Creates new views with R.layout.item_view as its template
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val layout = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_view, parent, false)
        return BookViewHolder(layout)
    }

    /**
     * Replaces the content of an existing view with new data
     */
    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val item = list.get(position)
        holder.button.text = item.toString()

        // Assigns a [OnClickListener] to the button contained in the [ViewHolder]
        holder.button.setOnClickListener {
            // Create an action from DetailList to DetailList
            // using the required arguments
            val action = BookListFragmentDirections.actionBookListFragmentToDetailListFragment(book = holder.button.text.toString())
            // Navigate using that action
            holder.view.findNavController().navigate(action)
        }
    }
}