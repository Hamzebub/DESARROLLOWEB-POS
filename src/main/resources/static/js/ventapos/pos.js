// ==========================================
// DATOS DE DEMOSTRACIÓN (reemplazar por datos del backend)
// ==========================================

/*
const DB_CATEGORIAS = [
    { id_categoria: 0, nombre: 'Todas' },
    { id_categoria: 1, nombre: 'Bebidas' },
    { id_categoria: 2, nombre: 'Snacks' },
    { id_categoria: 3, nombre: 'Abarrotes' },
    { id_categoria: 4, nombre: 'Lácteos' }
];
*/
const DB_PRODUCTOS = [
    { id_producto: 1, id_categoria: 1, sku: 'PROD-001', codigo_barras: '7750001001', nombre: 'Inka Kola 500ml', precio_venta: 3.50, stock_actual: 45, icon: 'fa-bottle-water', color: 'text-yellow-500' },
    { id_producto: 2, id_categoria: 1, sku: 'PROD-002', codigo_barras: '7750001002', nombre: 'Coca Cola Zero 1.5L', precio_venta: 7.20, stock_actual: 18, icon: 'fa-bottle-water', color: 'text-red-600' },
    { id_producto: 3, id_categoria: 2, sku: 'PROD-003', codigo_barras: '7750002001', nombre: 'Papas Lays Clásicas 160g', precio_venta: 5.50, stock_actual: 30, icon: 'fa-box-open', color: 'text-amber-500' },
    { id_producto: 4, id_categoria: 2, sku: 'PROD-004', codigo_barras: '7750002002', nombre: 'Galletas Ritz Pack x6', precio_venta: 4.20, stock_actual: 12, icon: 'fa-cookie', color: 'text-yellow-600' },
    { id_producto: 5, id_categoria: 3, sku: 'PROD-005', codigo_barras: '7750003001', nombre: 'Arroz Extra Costeño 1kg', precio_venta: 4.80, stock_actual: 80, icon: 'fa-bowl-rice', color: 'text-slate-600' },
    { id_producto: 6, id_categoria: 3, sku: 'PROD-006', codigo_barras: '7750003002', nombre: 'Aceite Primor Premium 1L', precio_venta: 9.90, stock_actual: 22, icon: 'fa-bottle-droplet', color: 'text-amber-400' },
    { id_producto: 7, id_categoria: 4, sku: 'PROD-007', codigo_barras: '7750004001', nombre: 'Leche Gloria Evaporada 400g', precio_venta: 4.10, stock_actual: 60, icon: 'fa-cow', color: 'text-blue-600' },
    { id_producto: 8, id_categoria: 4, sku: 'PROD-008', codigo_barras: '7750004002', nombre: 'Queso Edam Laive 200g', precio_venta: 11.50, stock_actual: 8, icon: 'fa-cheese', color: 'text-yellow-400' }
];

// Estado Global de la App
let activeCategory = 0;
let cart = []; // Almacena ítems para la tabla detalles_venta
let payments = []; // Almacena ítems para la tabla pagos
let sessionData = {
    id_caja: 1042,
    id_usuario: 1, // Cajero
    usuario_nombre: 'Carlos Mendoza'
};

// ==========================================
// INICIALIZACIÓN
// ==========================================
window.onload = function() {
    startClock();
    renderCategories();
    renderProducts();
    
    // Event listener para búsqueda en vivo
    document.getElementById('input-search').addEventListener('input', function() {
        renderProducts();
    });

    // Enter key para buscar/agregar producto directo si hay coincidencia única
    document.getElementById('input-search').addEventListener('keypress', function(e) {
        if (e.key === 'Enter') {
            const query = this.value.trim().toLowerCase();
            const matched = DB_PRODUCTOS.filter(p => 
                p.nombre.toLowerCase().includes(query) || 
                p.sku.toLowerCase().includes(query) || 
                p.codigo_barras.includes(query)
            );
            if (matched.length === 1) {
                addToCart(matched[0].id_producto);
                this.value = '';
                renderProducts();
            }
        }
    });
};

function startClock() {
    setInterval(() => {
        const now = new Date();
        document.getElementById('clock-date').innerText = now.toLocaleDateString('es-PE');
        document.getElementById('clock-time').innerText = now.toLocaleTimeString('es-PE');
    }, 1000);
}

// ==========================================
// CATÁLOGOS Y FILTROS
// ==========================================
function renderCategories2() {
    const container = document.getElementById('categories-bar');
    container.innerHTML = DB_CATEGORIAS.map(cat => `
        <button onclick="selectCategory(${cat.idCategoria})" 
            class="px-3 py-1 rounded-full text-xs font-semibold whitespace-nowrap transition-all ${
                activeCategory === cat.idCategoria 
                ? 'bg-indigo-600 text-white shadow-sm' 
                : 'bg-slate-200 text-slate-700 hover:bg-slate-300'
            }">
            ${cat.nombre}
        </button>
    `).join('');
}

function renderCategories(){
	const container = document.getElementById('categories-bar');
	let button=container.querySelector('.bg-indigo-600');
	button.classList.remove('bg-indigo-600','text-white','shadow-sm');
	button.classList.add('bg-slate-200','text-slate-700','hover:bg-slate-300');
	
	let buttonnuevo=container.querySelector(`[data-cat="${activeCategory}"]`);
	buttonnuevo.classList.remove('bg-slate-200','text-slate-700','hover:bg-slate-300');
	buttonnuevo.classList.add('bg-indigo-600','text-white','shadow-sm');

}

function selectCategory(idCat) {
    activeCategory = idCat;
    renderCategories();
    renderProducts();
}

function clearSearch() {
    document.getElementById('input-search').value = '';
    activeCategory = 0;
    renderCategories();
    renderProducts();
}

function renderProducts() {
    const container = document.getElementById('products-grid');
    const search = document.getElementById('input-search').value.toLowerCase().trim();

    const filtered = DB_PRODUCTOS.filter(prod => {
        const matchesCat = activeCategory === 0 || prod.id_categoria === activeCategory;
        const matchesSearch = prod.nombre.toLowerCase().includes(search) || 
                              prod.sku.toLowerCase().includes(search) || 
                              prod.codigo_barras.includes(search);
        return matchesCat && matchesSearch;
    });

    if (filtered.length === 0) {
        container.innerHTML = `
            <div class="col-span-full py-10 text-center text-slate-400">
                <i class="fa-solid fa-box-open text-3xl mb-2"></i>
                <p class="text-xs">No se encontraron productos coincidentes</p>
            </div>
        `;
        return;
    }

    container.innerHTML = filtered.map(prod => `
        <div onclick="addToCart(${prod.id_producto})" 
            class="bg-white border border-slate-200 hover:border-indigo-400 rounded-xl p-3 cursor-pointer transition-all hover:shadow-md flex flex-col justify-between group">
            <div>
                <div class="flex justify-between items-start mb-2">
                    <span class="text-[10px] font-mono bg-slate-100 text-slate-500 px-1.5 py-0.5 rounded">${prod.sku}</span>
                    <span class="text-[10px] font-semibold ${prod.stock_actual < 15 ? 'text-rose-500 bg-rose-50' : 'text-emerald-600 bg-emerald-50'} px-1.5 py-0.5 rounded">
                        Stk: ${prod.stock_actual}
                    </span>
                </div>
                <div class="text-center py-2">
                    <i class="fa-solid ${prod.icon} ${prod.color} text-2xl group-hover:scale-110 transition-transform"></i>
                </div>
                <h4 class="text-xs font-semibold text-slate-700 line-clamp-2 leading-tight">${prod.nombre}</h4>
            </div>
            <div class="mt-2 pt-2 border-t border-slate-100 flex justify-between items-center">
                <span class="text-xs text-slate-400">Precio:</span>
                <span class="text-sm font-bold text-indigo-600">S/ ${prod.precio_venta.toFixed(2)}</span>
            </div>
        </div>
    `).join('');
}

// ==========================================
// LÓGICA DEL CARRITO (detalles_venta)
// ==========================================
function addToCart(idProducto) {
    const product = DB_PRODUCTOS.find(p => p.id_producto === idProducto);
    if (!product) return;

    const existingItem = cart.find(item => item.id_producto === idProducto);

    if (existingItem) {
        if (existingItem.cantidad + 1 > product.stock_actual) {
            alert(`Stock insuficiente. Solo hay ${product.stock_actual} unidades disponibles.`);
            return;
        }
        existingItem.cantidad += 1;
        existingItem.subtotal = existingItem.cantidad * existingItem.precio_unitario;
    } else {
        cart.push({
            id_producto: product.id_producto,
            sku: product.sku,
            nombre: product.nombre,
            precio_unitario: product.precio_venta,
            cantidad: 1,
            subtotal: product.precio_venta
        });
    }

    renderCart();
}

function updateCartQuantity(idProducto, delta) {
    const item = cart.find(i => i.id_producto === idProducto);
    if (!item) return;

    const product = DB_PRODUCTOS.find(p => p.id_producto === idProducto);
    const newQty = item.cantidad + delta;

    if (newQty <= 0) {
        removeFromCart(idProducto);
        return;
    }

    if (newQty > product.stock_actual) {
        alert(`Stock alcanzado. Límite: ${product.stock_actual}`);
        return;
    }

    item.cantidad = newQty;
    item.subtotal = item.cantidad * item.precio_unitario;
    renderCart();
}

function removeFromCart(idProducto) {
    cart = cart.filter(i => i.id_producto !== idProducto);
    renderCart();
}

function clearCart() {
    if (cart.length > 0 && !confirm("¿Deseas vaciar el carrito de ventas?")) return;
    cart = [];
    renderCart();
}

function calculateTotals() {
    const total = cart.reduce((sum, i) => sum + i.subtotal, 0);
    // Cálculo IGV peruano estándar (Subtotal = Total / 1.18, IGV = Total - Subtotal)
    const subtotal = total / 1.18;
    const igv = total - subtotal;

    return {
        total: total,
        subtotal: subtotal,
        igv: igv
    };
}

function renderCart() {
    const container = document.getElementById('cart-items');

    if (cart.length === 0) {
        container.innerHTML = `
            <div class="h-full flex flex-col items-center justify-center text-slate-300 py-12">
                <i class="fa-solid fa-cart-arrow-down text-4xl mb-2"></i>
                <p class="text-xs font-medium">El carrito de compras está vacío</p>
                <p class="text-[10px] text-slate-400">Haz clic en los productos para agregarlos</p>
            </div>
        `;
    } else {
        container.innerHTML = cart.map(item => `
            <div class="py-2 px-1 flex items-center justify-between gap-2">
                <div class="flex-1 min-w-0">
                    <h5 class="text-xs font-semibold text-slate-800 truncate">${item.nombre}</h5>
                    <span class="text-[10px] text-slate-400">S/ ${item.precio_unitario.toFixed(2)} c/u</span>
                </div>
                <div class="flex items-center gap-1.5 bg-slate-100 p-1 rounded-md">
                    <button onclick="updateCartQuantity(${item.id_producto}, -1)" class="w-5 h-5 bg-white hover:bg-slate-200 text-slate-700 rounded flex items-center justify-center text-xs font-bold shadow-sm">-</button>
                    <span class="text-xs font-bold w-5 text-center font-mono">${item.cantidad}</span>
                    <button onclick="updateCartQuantity(${item.id_producto}, 1)" class="w-5 h-5 bg-white hover:bg-slate-200 text-slate-700 rounded flex items-center justify-center text-xs font-bold shadow-sm">+</button>
                </div>
                <div class="text-right min-w-[60px]">
                    <span class="text-xs font-bold text-slate-800 block">S/ ${item.subtotal.toFixed(2)}</span>
                    <button onclick="removeFromCart(${item.id_producto})" class="text-[10px] text-rose-500 hover:text-rose-700">Eliminar</button>
                </div>
            </div>
        `).join('');
    }

    const totals = calculateTotals();
    document.getElementById('lbl-subtotal').innerText = `S/ ${totals.subtotal.toFixed(2)}`;
    document.getElementById('lbl-igv').innerText = `S/ ${totals.igv.toFixed(2)}`;
    document.getElementById('lbl-total').innerText = `S/ ${totals.total.toFixed(2)}`;
}

// ==========================================
// LÓGICA DE PAGOS MÚLTIPLES (Tabla: pagos)
// ==========================================
function openPaymentModal() {
    if (cart.length === 0) {
        alert("Agrega al menos un producto al carrito antes de cobrar.");
        return;
    }

    const totals = calculateTotals();
    payments = []; // Reiniciar pagos

    document.getElementById('pay-total-due').innerText = `S/ ${totals.total.toFixed(2)}`;
    document.getElementById('pay-amount').value = totals.total.toFixed(2);
    document.getElementById('modal-pagos').classList.remove('hidden');
    document.getElementById('modal-pagos').classList.add('flex');

    updatePaymentSummary();
}

function closePaymentModal() {
    document.getElementById('modal-pagos').classList.add('hidden');
    document.getElementById('modal-pagos').classList.remove('flex');
}

function addPaymentRow() {
    const methodSelect = document.getElementById('pay-method');
    const amountInput = document.getElementById('pay-amount');
    const refInput = document.getElementById('pay-ref');

    const method = methodSelect.value;
    const amount = parseFloat(amountInput.value);
    const ref = refInput.value.trim();

    if (isNaN(amount) || amount <= 0) {
        alert("Ingrese un monto válido mayor a 0.");
        return;
    }

    payments.push({
        metodo_pago: method,
        monto: amount,
        referencia_transaccion: ref || 'S/N',
        estado: 'COMPLETADO'
    });

    // Reajustar sugerencia de monto restante para el siguiente pago
    const totals = calculateTotals();
    const currentPaid = payments.reduce((sum, p) => sum + p.monto, 0);
    const remaining = totals.total - currentPaid;

    amountInput.value = remaining > 0 ? remaining.toFixed(2) : '0.00';
    refInput.value = '';

    updatePaymentSummary();
}

function removePaymentRow(index) {
    payments.splice(index, 1);
    updatePaymentSummary();
}

function updatePaymentSummary() {
    const totals = calculateTotals();
    const totalPaid = payments.reduce((sum, p) => sum + p.monto, 0);
    const balance = totals.total - totalPaid;

    document.getElementById('pay-total-paid').innerText = `S/ ${totalPaid.toFixed(2)}`;

    const balanceLabel = document.getElementById('pay-balance');
    const btnComplete = document.getElementById('btn-complete-sale');

    if (balance <= 0) {
        // Pagado completamente o hay vuelto
        const vuelto = Math.abs(balance);
        balanceLabel.innerText = balance === 0 ? 'S/ 0.00' : `Vuelto: S/ ${vuelto.toFixed(2)}`;
        balanceLabel.className = 'font-mono font-bold text-sm text-indigo-600';
        
        // Habilitar botón de confirmar
        btnComplete.disabled = false;
        btnComplete.className = 'px-5 py-2 bg-emerald-600 hover:bg-emerald-500 text-white font-bold rounded-lg text-xs transition-all flex items-center gap-2 cursor-pointer shadow-lg';
    } else {
        // Falta cubrir
        balanceLabel.innerText = `Falta: S/ ${balance.toFixed(2)}`;
        balanceLabel.className = 'font-mono font-bold text-sm text-rose-600';

        // Deshabilitar botón
        btnComplete.disabled = true;
        btnComplete.className = 'px-5 py-2 bg-slate-400 text-white font-bold rounded-lg text-xs transition-all flex items-center gap-2 cursor-not-allowed';
    }

    // Renderizar tabla de pagos
    const tbody = document.getElementById('payment-rows');
    if (payments.length === 0) {
        tbody.innerHTML = `<tr><td colspan="4" class="p-3 text-center text-slate-400 italic">No hay medios de pago ingresados</td></tr>`;
    } else {
        tbody.innerHTML = payments.map((p, idx) => `
            <tr>
                <td class="p-2 font-semibold text-slate-700">${p.metodo_pago}</td>
                <td class="p-2 font-mono text-slate-500">${p.referencia_transaccion}</td>
                <td class="p-2 text-right font-bold text-slate-800">S/ ${p.monto.toFixed(2)}</td>
                <td class="p-2 text-center">
                    <button onclick="removePaymentRow(${idx})" class="text-rose-500 hover:text-rose-700"><i class="fa-solid fa-trash"></i></button>
                </td>
            </tr>
        `).join('');
    }
}

// ==========================================
// PROCESAR Y CONFIRMAR VENTA (JSON SQL POSTGRES)
// ==========================================
function completeSale() {
    const totals = calculateTotals();
    const clienteSelect = document.getElementById('select-cliente');
    const comprobanteSelect = document.getElementById('select-comprobante');
    const numComprobante = document.getElementById('input-num-comprobante').value;

    // Construir payload de demostración para enviar al backend Spring Boot
    const payloadVenta = {
        // Tabla: ventas
        venta: {
            id_usuario: sessionData.id_usuario,
            id_caja: sessionData.id_caja,
            id_cliente: parseInt(clienteSelect.value),
            numero_comprobante: numComprobante,
            tipo_comprobante: comprobanteSelect.value,
            fecha_hora: new Date().toISOString(),
            subtotal: parseFloat(totals.subtotal.toFixed(2)),
            igv: parseFloat(totals.igv.toFixed(2)),
            total: parseFloat(totals.total.toFixed(2)),
            estado: 'COMPLETADO'
        },
        // Tabla: detalles_venta
        detalles: cart.map(item => ({
            id_producto: item.id_producto,
            cantidad: item.cantidad,
            precio_unitario: item.precio_unitario,
            subtotal: item.subtotal
        })),
        // Tabla: pagos
        pagos: payments,
        // Kardex automático a insertar en PostgreSQL
        kardex_movimientos: cart.map(item => ({
            id_producto: item.id_producto,
            id_usuario: sessionData.id_usuario,
            tipo_movimiento: 'VENTA',
            cantidad: item.cantidad,
            referencia_origen: numComprobante
        }))
    };

    // Mostrar el resultado JSON
    document.getElementById('json-output').innerText = JSON.stringify(payloadVenta, null, 2);

    closePaymentModal();
    
    // Desplegar Modal del Ticket / JSON
    document.getElementById('modal-receipt').classList.remove('hidden');
    document.getElementById('modal-receipt').classList.add('flex');

    // Actualizar stock local simulado
    cart.forEach(item => {
        const prod = DB_PRODUCTOS.find(p => p.id_producto === item.id_producto);
        if (prod) prod.stock_actual -= item.cantidad;
    });
}

function closeReceiptModal() {
    document.getElementById('modal-receipt').classList.add('hidden');
    document.getElementById('modal-receipt').classList.remove('flex');

    // Resetear venta
    cart = [];
    payments = [];
    renderCart();
    renderProducts();

    // Incrementar correlativo
    const numInput = document.getElementById('input-num-comprobante');
    const parts = numInput.value.split('-');
    const nextNum = parseInt(parts[1]) + 1;
    numInput.value = `${parts[0]}-${String(nextNum).padStart(7, '0')}`;
}

function copyToClipboard() {
    const jsonText = document.getElementById('json-output').innerText;
    navigator.clipboard.writeText(jsonText).then(() => {
        alert("JSON copiado al portapapeles exitosamente.");
    });
}

function openNewClientModal() {
    const nombre = prompt("Ingrese Nombre o Razón Social del nuevo cliente:");
    if (!nombre) return;
    const doc = prompt("Ingrese DNI o RUC:");

    const select = document.getElementById('select-cliente');
    const newOpt = document.createElement('option');
    newOpt.value = Date.now();
    newOpt.innerText = `${nombre} (${doc || 'SIN DOC'})`;
    newOpt.selected = true;
    select.appendChild(newOpt);
}

function toggleDColoresMenu(force) {
    const sidebar = document.getElementById('dcSidebar');
    const overlay = document.getElementById('dcMenuOverlay');
    const shouldOpen = typeof force === 'boolean' ? force : !sidebar.classList.contains('open');
    sidebar.classList.toggle('open', shouldOpen);
    overlay.classList.toggle('open', shouldOpen);
}

function selectModule(button, moduleName) {
    document.querySelectorAll('.dc-nav button').forEach(item => item.classList.remove('active'));
    button.classList.add('active');
    if (moduleName !== 'Ventas (POS)') {
        const notice = document.getElementById('module-notice');
        notice.textContent = `Módulo "${moduleName}": disponible para integrar en la siguiente vista.`;
        notice.style.display = 'block';
        clearTimeout(window.moduleNoticeTimer);
        window.moduleNoticeTimer = setTimeout(() => notice.style.display = 'none', 3000);
    }
    toggleDColoresMenu(false);
}
