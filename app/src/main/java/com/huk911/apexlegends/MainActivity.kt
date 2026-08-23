package com.huk911.apexlegends

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.graphics.Color
import android.media.SoundPool
import android.util.Log
import android.widget.ImageButton
import android.widget.ProgressBar
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.huk911.apexlegends.models.Consumable
import com.huk911.apexlegends.models.Floor
import com.huk911.apexlegends.models.Grenade
import com.huk911.apexlegends.models.Inventory
import com.huk911.apexlegends.models.Item
import com.huk911.apexlegends.models.KnockdownWatcher
import com.huk911.apexlegends.models.Recyclable
import com.huk911.apexlegends.models.SelectionWatcher
import com.huk911.apexlegends.models.Weapon

class MainActivity : AppCompatActivity(), KnockdownWatcher, SelectionWatcher {

    private val viewModel: MainViewModel by viewModels()
    private lateinit var healthBarCard: TextView
    private lateinit var infoCard: TextView
    private lateinit var handCard: TextView
    private lateinit var secHandCard: TextView
    private lateinit var inventoryCounter: TextView
    private lateinit var floorCounter: TextView
    private lateinit var materialsCard: TextView
    private lateinit var knockdownBanner: TextView
    private lateinit var recycleButton: Button
    private lateinit var recycleFloorButton: Button
    private lateinit var useButton: Button
    private lateinit var dropButton: Button
    private lateinit var pickButton: Button
    private lateinit var equipButton: Button
    private lateinit var dropHandButton: Button
    private lateinit var dropSecHandButton: Button
    private lateinit var inspectButton: Button
    private lateinit var inspectFloorButton: Button
    private lateinit var healthProgress: ProgressBar
    private lateinit var swapButton: ImageButton
    private lateinit var soundPool: SoundPool
    private var knockdownSound: Int = 0
    private lateinit var backpackList: RecyclerView
    private lateinit var floorList: RecyclerView


    private lateinit var backpackAdapter: BackpackAdapter
    private lateinit var floorAdapter: FloorAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        Log.i("govno", this.toString())
        Log.i("govno", viewModel.toString())

        applyInsets()

        backpackAdapter = BackpackAdapter(viewModel.inventory)
        floorAdapter = FloorAdapter(viewModel.floor)



        backpackList = findViewById(R.id.rv_backpack)
        backpackList.layoutManager = LinearLayoutManager(this)
        backpackList.adapter = backpackAdapter

        floorList = findViewById(R.id.rv_floor)
        floorList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        floorList.adapter = floorAdapter


        recycleButton = findViewById(R.id.btn_recycle)
        materialsCard = findViewById(R.id.tv_materials)

        healthBarCard = findViewById(R.id.tv_health_bar)
        infoCard = findViewById(R.id.tv_info_card)

        useButton = findViewById(R.id.btn_use)
        dropButton = findViewById(R.id.btn_drop)
        swapButton = findViewById(R.id.btn_swap)

        pickButton = findViewById(R.id.btn_floor_pick)
        recycleFloorButton = findViewById(R.id.btn_recycle_floor)
        inspectButton = findViewById(R.id.btn_inspect)
        inspectFloorButton = findViewById(R.id.btn_inspect_floor)

        knockdownBanner = findViewById(R.id.tv_knockdown_banner)
        handCard = findViewById(R.id.tv_main_hand_card)
        secHandCard = findViewById(R.id.tv_sec_hand_card)
        equipButton = findViewById(R.id.btn_equip)
        dropHandButton = findViewById(R.id.btn_drop_from_hand)
        dropSecHandButton = findViewById(R.id.btn_drop_from_sec_hand)
        healthProgress = findViewById(R.id.healthProgress)
        inventoryCounter = findViewById(R.id.tv_items_counter)
        floorCounter = findViewById(R.id.tv_floor_items_counter)

        healthProgress.progress = viewModel.inventory.health
        viewModel.inventory.knockdownWatcher = this
        backpackAdapter.selectionWatcher = this
        floorAdapter.selectionWatcher = this

        swapButton.setOnClickListener {
            viewModel.inventory.swapWeapon()
            showInfo("Оружие свапнуто")
            renderHands()
        }

        soundPool = SoundPool.Builder()
            .setMaxStreams(5)
            .build()

        knockdownSound = soundPool.load(this, R.raw.knockdown_sound, 1)

        recycleButton.setOnClickListener {
            val recycledItem = viewModel.inventory.recycleCurrentSelectedItem()
            if (recycledItem is Recyclable) {
               val scrapMaterials = recycledItem.calculateScrapMaterials()
               showInfo("Переработано: " + recycledItem.name + "+ " + scrapMaterials + " материалов")
            } else {
                showInfo("Это нельзя переработать")
            }
            renderBackpack()
        }

        recycleFloorButton.setOnClickListener {
            val selectedItem = viewModel.floor.currentSelectedItem
            if (selectedItem is Recyclable) {
                viewModel.floor.takeCurrentSelectedItem()
                viewModel.inventory.recycle(selectedItem)
                val scrapMaterials = selectedItem.calculateScrapMaterials()
                showInfo("Переработано: " + selectedItem.name + " + " + scrapMaterials + " Материалов")
                renderFloor()
            } else {
                showInfo("Это нельзя переработать")
            }
            renderBackpack()
        }

        inspectButton.setOnClickListener {
            val selectedItem = viewModel.inventory.currentSelectedItem
            if (selectedItem != null) {
                val intent = Intent(this, ItemDetailActivity::class.java)
                intent.putExtra(EXTRA_ITEM, selectedItem)
                startActivity(intent)
            } else {
                showInfo("Нечего осматривать")
            }
        }

        inspectFloorButton.setOnClickListener {
            val selectedItem = viewModel.floor.currentSelectedItem
            if (selectedItem != null) {
                val intent = Intent(this, ItemDetailActivity::class.java)
                intent.putExtra(EXTRA_ITEM, selectedItem)
                startActivity(intent)
            } else {
                showInfo("Нечего осматривать")
            }
        }



        dropHandButton.setOnClickListener {
            val droppedItem = viewModel.inventory.dropPrimaryWeapon()
            if (droppedItem != null) {
                viewModel.floor.addItem(droppedItem)
                showInfo("Предмет выброшен: $droppedItem")
                renderFloor()
                renderHands()
            } else {
                showInfo("Нечего выбрасывать")
            }

        }

        dropSecHandButton.setOnClickListener {
            val droppedItem = viewModel.inventory.dropSecondaryWeapon()
            if (droppedItem != null) {
                viewModel.floor.addItem(droppedItem)
                showInfo("Предмет выброшен: $droppedItem")
                renderFloor()
                renderHands()
            } else {
                showInfo("Нечего выбрасывать")
            }

        }

        equipButton.setOnClickListener {
            val equippedItem = viewModel.inventory.equipSelectedWeapon()
            if (equippedItem != null) {
                renderHands()
                renderBackpack()
                showInfo("Предмет взят в руку: $equippedItem")
            } else {
                showInfo("Нечего эквипать")
            }
        }

        useButton.setOnClickListener {
            val itemUsed = viewModel.inventory.useCurrentSelectedItem()
            when (itemUsed) {
                is Consumable -> showInfo("Подхилено: +" + itemUsed.healAmount + " HP")
                is Grenade -> showInfo("Подзворвано: -" + itemUsed.blastDamage + " HP")
            }
            renderBackpack()
            renderHealth()
        }

        pickButton.setOnClickListener {
            val pickedItem = viewModel.floor.takeCurrentSelectedItem()
            if (pickedItem != null) {
                viewModel.inventory.pickUp(pickedItem)
                renderBackpack()
                renderFloor()
                showInfo("Подобран предмет: $pickedItem")
            } else {
                showInfo("Нечего поднимать")
            }
        }

        dropButton.setOnClickListener {
            val droppedItem = viewModel.inventory.dropItemFromBackpack()
            if (droppedItem != null) {
                viewModel.floor.addItem(droppedItem)
                showInfo("Предмет выброшен: $droppedItem")
                renderFloor()
                renderBackpack()
            } else showInfo("Нечего выбрасывать")
        }
        renderScreen()
    }

    override fun onPause() {
        super.onPause()
        viewModel.saveGame()
    }

    private fun applyInsets() {
        val rootView = findViewById<View>(R.id.main)
        val basePaddingLeft = rootView.paddingLeft
        val basePaddingTop = rootView.paddingTop
        val basePaddingRight = rootView.paddingRight
        val basePaddingBottom = rootView.paddingBottom
        ViewCompat.setOnApplyWindowInsetsListener(rootView) { view, windowInsets ->
            val systemBars = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(
                basePaddingLeft + systemBars.left,
                basePaddingTop + systemBars.top,
                basePaddingRight + systemBars.right,
                basePaddingBottom + systemBars.bottom
            )
            WindowInsetsCompat.CONSUMED
        }
    }

    private fun renderHands() {
        handCard.text = viewModel.inventory.primaryWeapon?.toString() ?: "Рука пуста"
        secHandCard.text = viewModel.inventory.secondaryWeapon?.toString() ?: "Рука пуста"
        dropHandButton.isEnabled = viewModel.inventory.primaryWeapon != null
        dropSecHandButton.isEnabled = viewModel.inventory.secondaryWeapon != null
        swapButton.isEnabled = viewModel.inventory.primaryWeapon != null || viewModel.inventory.secondaryWeapon != null
    }

    private fun renderBackpack() {
        val isBackpackEmpty = viewModel.inventory.backpack.isEmpty()
        inventoryCounter.text = "Предметов: " + viewModel.inventory.backpack.size
        backpackAdapter.notifyDataSetChanged()
        equipButton.isEnabled = !isBackpackEmpty && viewModel.inventory.currentSelectedItem is Weapon
        useButton.isEnabled = !isBackpackEmpty
        dropButton.isEnabled = !isBackpackEmpty
        inspectButton.isEnabled = !isBackpackEmpty
        recycleButton.isEnabled = viewModel.inventory.currentSelectedItem is Recyclable
        materialsCard.text = "Материалов: " + viewModel.inventory.materials
    }


    private fun renderFloor() {
        val isFloorEmpty = viewModel.floor.items.isEmpty()
        floorCounter.text = "Предметов на полу: " + viewModel.floor.items.size
        floorAdapter.notifyDataSetChanged()
        pickButton.isEnabled = !isFloorEmpty
        recycleFloorButton.isEnabled = viewModel.floor.currentSelectedItem is Recyclable
    }

    private fun renderHealth() {
        healthBarCard.text = "HP: " + viewModel.inventory.health + ", Нокдаунов: " + viewModel.inventory.knockdownCounter
        healthProgress.progress = viewModel.inventory.health
        val healthColor = pickHealthColor(viewModel.inventory.health)
        healthProgress.progressTintList = ColorStateList.valueOf(healthColor)
        if (viewModel.inventory.health == 0) {
            knockdownBanner.visibility = View.VISIBLE
        } else {
            knockdownBanner.visibility = View.GONE
        }
    }

    private fun renderScreen() {
        renderHands()
        renderBackpack()
        renderFloor()
        renderHealth()
    }

    private fun showInfo(message: String) {
        infoCard.text = message
    }

    fun pickHealthColor(health: Int): Int = when {
        health >= 70 -> Color.GREEN
        health >= 30 -> Color.MAGENTA
        else -> Color.RED
    }

    override fun onPlayerKnocked() {
        Log.i("govno", "knocked")
        soundPool.play(knockdownSound, 1f, 1f, 1, 0, 1f)
    }

    override fun onPlayerRevived() {
        Log.i("govno", "revived")
    }

    override fun onItemSelected(item: Item) {
        renderBackpack()
        renderFloor()
    }
}
