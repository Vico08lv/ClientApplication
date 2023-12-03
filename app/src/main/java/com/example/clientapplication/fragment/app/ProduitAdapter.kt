package com.example.clientapplication.fragment.app

import android.app.Dialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.clientapplication.AppViewModel
import com.example.clientapplication.R
import com.example.clientapplication.localStorage.Storage
import com.example.clientapplication.model.StatusProduitQuantite
import com.example.clientapplication.model.request.ProduitQuantiteRequest
import com.example.clientapplication.model.response.ProduitQuantiteResponse
import com.example.clientapplication.model.response.ProduitResponse


class ProduitAdapter(
    var products: List<ProduitResponse>,
    var context: Context,
    var appViewModel: AppViewModel
) : RecyclerView.Adapter<ProduitAdapter.ProductViewHolder>() {

    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var produit_nom: TextView = itemView.findViewById(R.id.produit_nom)
        var produit_prix: TextView = itemView.findViewById(R.id.produit_prix)
        var produit_description: TextView = itemView.findViewById(R.id.produit_description)
        var produit_producteur: TextView = itemView.findViewById(R.id.produit_producteur)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.carte_produit, parent, false)
        val viewHolder = ProductViewHolder(view)
        return viewHolder
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.produit_nom.text = products[position].nom
        holder.produit_prix.text = products[position].prix.toString()
        holder.produit_producteur.text = products[position].emailProducteur
        holder.produit_description.text = products[position].description

        holder.itemView.setOnClickListener {
            // Gérer le clic sur un élément de la liste
            showProductDetailsDialog(products[position])
        }

    }

    override fun getItemCount(): Int {
        return products.size
    }

    private fun showProductDetailsDialog(product: ProduitResponse) {
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.details_produit)

        val dialogProduitNom: TextView = dialog.findViewById(R.id.dialog_produit_nom)
        val dialogProduitPrix: TextView = dialog.findViewById(R.id.dialog_produit_prix)
        val dialogProduitDescription: TextView = dialog.findViewById(R.id.dialog_produit_description)
        val dialogProduitProducteur: TextView = dialog.findViewById(R.id.dialog_produit_producteur)
        val dialogProduitQuantite: TextView = dialog.findViewById(R.id.dialog_produit_quantite)

        // Mettre à jour les vues du dialogue avec les détails du produit sélectionné
        dialogProduitNom.text = product.nom
        dialogProduitPrix.text = product.prix.toString()
        dialogProduitProducteur.text = product.emailProducteur
        dialogProduitDescription.text = product.description
        dialogProduitQuantite.text = product.quantite.toString()

        val btnMinus: ImageButton = dialog.findViewById(R.id.btnMinus)
        val btnPlus: ImageButton = dialog.findViewById(R.id.btnPlus)
        val editQuantity: EditText = dialog.findViewById(R.id.editQuantity)

        var quantity = 0

        btnMinus.setOnClickListener {
            if (quantity > 0) {
                quantity--
                editQuantity.setText(quantity.toString())
            }
        }

        btnPlus.setOnClickListener {
            quantity++
            editQuantity.setText(quantity.toString())
        }

        val ajouterPanier: Button = dialog.findViewById(R.id.ajouterPanier)

        ajouterPanier.setOnClickListener {
            val produitQuantite = ProduitQuantiteRequest(
                id = product.id,
                quantite = quantity,
                statusProduitQuantite = StatusProduitQuantite.ACCEPT
            )

            // Appelle la fonction de l'interface pour gérer l'ajout au panier
            product.emailProducteur?.let { it1 -> appViewModel.addToCart(produitQuantite, it1) }

        }



        // Réglage des paramètres de la fenêtre du dialog
        val window = dialog.window
        val layoutParams = window?.attributes
        layoutParams?.width = WindowManager.LayoutParams.MATCH_PARENT // Largeur de la fenêtre
        layoutParams?.height = WindowManager.LayoutParams.WRAP_CONTENT // Hauteur de la fenêtre
        window?.attributes = layoutParams

        // Afficher le dialogue
        dialog.show()
    }

    fun updateProducts(newProducts: List<ProduitResponse>) {
        products = newProducts
        notifyDataSetChanged()
    }


}