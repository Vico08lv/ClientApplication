package com.example.clientapplication.fragment.app

import android.app.Dialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.clientapplication.R
import com.example.clientapplication.model.request.CommandeRequest


class CartAdapter(
    var panier: List<CommandeRequest>,
    var context: Context
) : RecyclerView.Adapter<CartAdapter.CommandeViewHolder>() {

    class CommandeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        var commande_producteur: TextView = itemView.findViewById(R.id.commande_producteur)
//        var commande_tot_produit: TextView = itemView.findViewById(R.id.commande_tot_produit)

//        var panier_produit_id: TextView = itemView.findViewById(R.id.panier_produit_id)
//        var panier_produit_qtt: TextView = itemView.findViewById(R.id.panier_produit_qtt)
//        var panier_produit_total: TextView = itemView.findViewById(R.id.panier_produit_total)
        var panier_produit_info: TextView = itemView.findViewById(R.id.panier_produit_info)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommandeViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.carte_panier, parent, false)
        val viewHolder = CommandeViewHolder(view)
        return viewHolder
    }



    override fun onBindViewHolder(holder: CommandeViewHolder, position: Int) {
//        holder.commande_producteur.text = panier[position].PRODUCTEUR.toString()
//        holder.commande_tot_produit.text = panier[position].date.toString()

        val currentCommande = panier[position]
        val produitsText = StringBuilder()
        for (produit in currentCommande.produits!!) {
            produitsText.append("ID: ${produit.id}, Quantité: ${produit.quantite}\n")
        }

        holder.panier_produit_info.text = produitsText.toString()

    }




    fun updatePanier(newPanier: List<CommandeRequest>) {
        panier = newPanier
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return panier.size
    }


}