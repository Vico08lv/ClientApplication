package com.example.clientapplication.fragment.app

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.clientapplication.AppViewModel
import com.example.clientapplication.R
import com.example.clientapplication.model.StatusCommande
import com.example.clientapplication.model.response.CommandeResponse
import com.example.clientapplication.model.response.CommandesResponse
import java.text.SimpleDateFormat
import java.util.Locale


class CommandeAdapter(
    var commandes: CommandesResponse,
    var context: Context
) : RecyclerView.Adapter<CommandeAdapter.CommandesViewHolder>() {

    class CommandesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var commande_id: TextView = itemView.findViewById(R.id.commande_id)
        var commande_status: TextView = itemView.findViewById(R.id.commande_status)
        var commande_date: TextView = itemView.findViewById(R.id.commande_date)
        var commande_quantite: TextView = itemView.findViewById(R.id.commande_quantite)
        var commande_status_color : LinearLayout = itemView.findViewById(R.id.statusCommandeColor)
//        var commande_produits: TextView = itemView.findViewById(R.id.commande_produits)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommandesViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.carte_commande, parent, false)
        val viewHolder = CommandesViewHolder(view)
        return viewHolder
    }

    override fun onBindViewHolder(holder: CommandesViewHolder, position: Int) {
        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())

        holder.commande_id.text = commandes.commandes?.get(position)?.id.toString()
        holder.commande_status.text = commandes.commandes?.get(position)?.status.toString()

        val status = commandes.commandes?.get(position)?.status
        val backgroundColor = when (status) {
            StatusCommande.EN_ATTENTE_DE_VALIDATION -> R.color.status_en_attente
            StatusCommande.VALIDE -> R.color.status_valide
            StatusCommande.REFUS -> R.color.status_refus
            else -> android.R.color.white // Couleur par défaut si le statut n'est pas défini
        }
        holder.commande_status_color.setBackgroundColor(ContextCompat.getColor(context, backgroundColor))

        holder.commande_date.text = commandes.commandes?.get(position)?.date?.let { date ->
            dateFormat.format(date).toString()
        } ?: ""

        var sommeQuantites = 0
        commandes.commandes?.get(position)?.produits?.forEach { produit ->
            produit.quantite?.let { q ->
                sommeQuantites += q
            }
        }

        holder.commande_quantite.text = sommeQuantites.toString()
//        holder.commande_produits.text = commandes[position].produits dois boucler BOUTON POUR AFFICHER DETAIL COMMANDE
    }

    override fun getItemCount(): Int {
        return commandes.commandes?.size ?: 0
    }

    fun updateCommandes(newCommandes: CommandesResponse) {
        commandes = newCommandes
        notifyDataSetChanged()
    }


}