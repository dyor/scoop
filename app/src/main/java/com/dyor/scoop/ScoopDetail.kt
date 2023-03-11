package com.dyor.scoop


import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore


class ScoopDetail : AppCompatActivity() {

  private lateinit var db: FirebaseFirestore
  var scoop = Scoop()
  var scoopName = "unknown"
  private lateinit var memberArrayList: ArrayList<Member>
  var buyerPosition = -1
  var members = ""
  private lateinit var recyclerView: RecyclerView
  private lateinit var jobArrayList: ArrayList<Job>
  private lateinit var jobAdapter: JobAdapter

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_scoop_detail)
    //val scoopId = intent.getStringExtra("scoopId")
    val itemIdTextView = findViewById<TextView>(R.id.job_name)
    val itemIdTextDescription = findViewById<TextView>(R.id.job_description)
    val itemBack = findViewById<Button>(R.id.btnBackJob)
    itemBack.setOnClickListener {
      val intent = Intent(this, ScoopList::class.java)
      startActivity(intent)
    }
    var scoop = intent.getParcelableExtra("scoop", Scoop::class.java)
    itemIdTextView.text = scoop!!.scoopName
    itemIdTextDescription.text = scoop!!.description
    setUsers(scoop)
    setJobs(scoop)
    val itemJobCreate = findViewById<Button>(R.id.btnAddJob)
    itemJobCreate.setOnClickListener {
      val intent = Intent(this, JobCreate::class.java)
      intent.putExtra("scoop", scoop)
      startActivity(intent)
    }
  }

  private fun setJobs(scoop: Scoop) {
    if (scoop.jobs!=null) {

      recyclerView = findViewById(R.id.rvJobs)
      recyclerView.layoutManager = LinearLayoutManager(this)
      recyclerView.setHasFixedSize(true)

      //jobArrayList = arrayListOf()
      jobArrayList = scoop.jobs as ArrayList<Job>
      jobAdapter = JobAdapter(jobArrayList) {
        val intent = Intent(this, JobDetail::class.java)
        intent.putExtra("scoop", scoop)
        intent.putExtra("job", it)
        startActivity(intent)
      }
      recyclerView.adapter = jobAdapter

    }
  }

  private fun setUsers(scoop: Scoop) {
    var member : Member
    members = ""
    if (scoop.buyers!=null) {
      for (buyer: Buyer in scoop.buyers!!) {
        members += "\nbuyer: " + buyer.buyerName
      }
    }
    if (scoop.sellers!=null) {
      for (seller: Seller in scoop.sellers!!) {
        members += "\nseller: " + seller.sellerName
      }
    }
    val itemIdTextMember = findViewById<TextView>(R.id.scoop_members)
    itemIdTextMember.text = members

    db= FirebaseFirestore.getInstance()
    //val uid = FirebaseAuth.getInstance().currentUser?.uid.toString()
    memberArrayList = arrayListOf()
    db.collection("users")
      .get()
      .addOnSuccessListener { documents ->
        for (document in documents) {
          Log.d(TAG, "${document.id} => oyy ${document.data}")
          member = document.toObject(Member::class.java)!!
          memberArrayList.add(member)
        }
        //handle buyers
        val  itemAddBuyer = findViewById<Spinner>(R.id.addBuyer)
        if (itemAddBuyer != null) {
          val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, memberArrayList.map { member -> member.memberName })
          itemAddBuyer.adapter = adapter
        }
        val addBuyerButton = findViewById<Button>(R.id.btnAddBuyer)
        addBuyerButton.setOnClickListener {
          Toast.makeText(
            this@ScoopDetail, // getString(R.string.selected_item) + " " +
            "Buyer added " + memberArrayList[buyerPosition].memberName, Toast.LENGTH_SHORT
          ).show()
          val buyer: Buyer = Buyer(
            memberArrayList[buyerPosition].memberName,
            memberArrayList[buyerPosition].memberUid,
            Timestamp.now().toString()
          )
          var newBuyers: ArrayList<Buyer> = arrayListOf()
          if (scoop.buyers != null) {
            for (thisBuyer in scoop.buyers!!) {
              newBuyers.add(thisBuyer)
            }
          }
          newBuyers.add(buyer)
          scoop.buyers = newBuyers
          db.collection("scoops").document(scoop.scoopId!!).set(scoop)
          setUserIds(scoop!!.scoopId)
          setUsers(scoop)
        }
        val addSellerButton = findViewById<Button>(R.id.btnAddSeller)
        addSellerButton.setOnClickListener {
          Toast.makeText(
            this@ScoopDetail, // getString(R.string.selected_item) + " " +
            "Seller added " + memberArrayList[buyerPosition].memberName, Toast.LENGTH_SHORT
          ).show()
          val seller: Seller = Seller(
            memberArrayList[buyerPosition].memberName,
            memberArrayList[buyerPosition].memberUid,
            Timestamp.now().toString()
          )
          var newSellers: ArrayList<Seller> = arrayListOf()
          if (scoop.sellers != null) {
            for (thisSeller in scoop.sellers!!) {
              newSellers.add(thisSeller)
            }
          }
          newSellers.add(seller)
          scoop.sellers = newSellers
          db.collection("scoops").document(scoop.scoopId!!).set(scoop)
          setUserIds(scoop!!.scoopId)
          setUsers(scoop)
        }

        itemAddBuyer.onItemSelectedListener = object :
          AdapterView.OnItemSelectedListener {
          override fun onItemSelected(
            parent: AdapterView<*>?,
            view: android.view.View?,
            position: Int,
            id: Long
          ) {
            //Toast.makeText(this@ScoopDetail, "You scooped boy!", Toast.LENGTH_LONG).show()
            //add the new buyer
            buyerPosition = position
          }
          override fun onNothingSelected(parent: AdapterView<*>?) {
            //TODO("Not yet implemented")
          }
        }
      }
      .addOnFailureListener { exception ->
        Log.w(TAG, "Error getting documents: ", exception)
      }
  }


  private fun setUserIds(scoopId: kotlin.String?) {
    db= FirebaseFirestore.getInstance()
    var scoop : Scoop
    if (scoopId != null) {
      db.collection("scoops").document(scoopId).get()
        .addOnSuccessListener { document ->
          scoop = document.toObject(Scoop::class.java)!!
          //not sure why scoopId is not getting populated - so manually setting
          scoop.scoopId = scoopId
          setUserIdsCallback(scoop)
        }
    }
  }

  private fun setUserIdsCallback(scoop: Scoop){
    var userIds: ArrayList<String> = arrayListOf()
    //gather up the manager id
    if(scoop.scoopManager!=null) {
      userIds.add(scoop.scoopManager!!)
    }
    //gather up the buyer and seller ids
    if (scoop.buyers!=null) {
      for (buyer: Buyer in scoop.buyers!!) {
        buyer.buyerManager?.let { userIds.add(it) }
      }
    }
    if (scoop.sellers!=null) {
      for (seller: Seller in scoop.sellers!!) {
        seller.sellerManager?.let { userIds.add(it) }
      }
    }
    Log.d(TAG, "DocumentSnapshot data: ${userIds}")
    scoop.userIds = userIds
    db.collection("scoops").document(scoop.scoopId!!).set(scoop)
    Log.d(TAG, "left it!!: $scoop")
  }
}

